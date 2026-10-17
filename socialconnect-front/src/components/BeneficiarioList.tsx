import { useEffect, useState } from 'react';
import api, { describeApiError } from '../services/api';
import type { Beneficiario, Page } from '../types/beneficiario';

interface Props { refreshKey: number; onCreate: () => void }

export function BeneficiarioList({ refreshKey, onCreate }: Props) {
  const [data, setData] = useState<Page<Beneficiario> | null>(null);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [retry, setRetry] = useState(0);

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
    setError('');
    api.get<Page<Beneficiario>>('/beneficiarios', {
      params: { page, size: 10 }, signal: controller.signal,
    }).then(response => {
      if (!controller.signal.aborted) setData(response.data);
    }).catch((err: unknown) => {
      if (!controller.signal.aborted) setError(describeApiError(err));
    }).finally(() => {
      if (!controller.signal.aborted) setLoading(false);
    });
    return () => controller.abort();
  }, [page, refreshKey, retry]);

  return (
    <section className="panel" aria-labelledby="list-title" aria-busy={loading}>
      <div className="panel-heading">
        <div><h2 id="list-title">Pessoas cadastradas</h2><p>Consulte os beneficiários atendidos pela instituição.</p></div>
        <span className="count">{data ? `${data.totalElements} ${data.totalElements === 1 ? 'cadastro' : 'cadastros'}` : 'Beneficiários'}</span>
      </div>
      {loading ? <div className="state" role="status">Carregando beneficiários…</div> : error ? (
        <div className="state"><p role="alert">{error}</p><button className="button secondary" onClick={() => setRetry(value => value + 1)}>Tentar novamente</button></div>
      ) : !data?.content.length ? (
        <div className="state"><span className="empty-icon" aria-hidden="true">＋</span><h3>Nenhum beneficiário por aqui</h3><p>Comece cadastrando a primeira pessoa atendida.</p><button className="button" onClick={onCreate}>Cadastrar beneficiário</button></div>
      ) : (
        <>
          <div className="table-scroll" tabIndex={0} role="region" aria-label="Tabela de beneficiários">
            <table>
              <caption className="sr-only">Beneficiários cadastrados, página {page + 1}</caption>
              <thead><tr><th scope="col">Nome</th><th scope="col">CPF</th><th scope="col">Telefone</th><th scope="col">Endereço</th></tr></thead>
              <tbody>{data.content.map(item => (
                <tr key={item.idBeneficiario}><td><strong>{item.nome}</strong><span className="record-id">Cadastro #{item.idBeneficiario}</span></td><td>{item.cpf.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})$/, '$1.$2.$3-$4')}</td><td>{item.telefone || 'Não informado'}</td><td>{item.endereco || 'Não informado'}</td></tr>
              ))}</tbody>
            </table>
          </div>
          <div className="pagination">
            <p>Página {data.number + 1} de {Math.max(data.totalPages, 1)} · {data.totalElements} {data.totalElements === 1 ? 'registro' : 'registros'}</p>
            <div><button className="button secondary" disabled={page === 0} onClick={() => setPage(value => value - 1)}>Anterior</button><button className="button secondary" disabled={page + 1 >= data.totalPages} onClick={() => setPage(value => value + 1)}>Próxima</button></div>
          </div>
        </>
      )}
    </section>
  );
}
