import { useRef, useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';
import api, { describeApiError } from '../services/api';
import type { ApiProblem, Beneficiario, BeneficiarioRequest } from '../types/beneficiario';

interface Props { onSuccess: (nome: string) => void; onCancel: () => void }
type Errors = Partial<Record<keyof BeneficiarioRequest, string>>;

export function BeneficiarioForm({ onSuccess, onCancel }: Props) {
  const [nome, setNome] = useState('');
  const [cpf, setCpf] = useState('');
  const [telefone, setTelefone] = useState('');
  const [endereco, setEndereco] = useState('');
  const [errors, setErrors] = useState<Errors>({});
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const submitting = useRef(false);
  const formRef = useRef<HTMLFormElement>(null);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (submitting.current) return;
    const payload: BeneficiarioRequest = { nome: nome.trim(), cpf: cpf.replace(/\D/g, ''), telefone: telefone.trim(), endereco: endereco.trim() };
    const nextErrors: Errors = {};
    if (!payload.nome) nextErrors.nome = 'Informe o nome completo.';
    if (!payload.cpf) nextErrors.cpf = 'Informe o CPF.';
    else if (payload.cpf.length !== 11) nextErrors.cpf = 'O CPF deve conter 11 dígitos.';
    if (!payload.telefone) nextErrors.telefone = 'Informe o telefone com DDD.';
    if (!payload.endereco) nextErrors.endereco = 'Informe o endereço.';
    setErrors(nextErrors);
    setError('');
    if (Object.keys(nextErrors).length) {
      const first = Object.keys(nextErrors)[0];
      formRef.current?.querySelector<HTMLInputElement>(`[name="${first}"]`)?.focus();
      return;
    }
    submitting.current = true;
    setSaving(true);
    try {
      const response = await api.post<Beneficiario>('/beneficiarios', payload);
      onSuccess(response.data.nome);
    } catch (err: unknown) {
      setError(describeApiError(err));
      if (axios.isAxiosError<ApiProblem>(err)) {
        const fieldErrors: Errors = {};
        for (const item of err.response?.data.errors || []) {
          if (['nome', 'cpf', 'telefone', 'endereco'].includes(item.field)) fieldErrors[item.field as keyof Errors] = item.message;
        }
        if (err.response?.status === 409) fieldErrors.cpf = 'CPF já cadastrado.';
        setErrors(fieldErrors);
      }
    } finally {
      submitting.current = false;
      setSaving(false);
    }
  };

  return (
    <section className="panel form-panel" aria-labelledby="form-title">
      <div className="panel-heading"><div><h2 id="form-title">Dados do beneficiário</h2><p>Todos os campos são obrigatórios.</p></div><span className="count">Novo cadastro</span></div>
      <form ref={formRef} onSubmit={handleSubmit} noValidate>
        {error && <div className="notice error" role="alert">{error}</div>}
        <fieldset disabled={saving}>
          <legend className="sr-only">Informações pessoais e de contato</legend>
          <div className="form-grid">
            <div className="field wide"><label htmlFor="nome">Nome completo</label><input id="nome" name="nome" autoComplete="name" autoFocus required maxLength={150} value={nome} onChange={e => setNome(e.target.value)} aria-invalid={!!errors.nome} aria-describedby={errors.nome ? 'nome-error' : undefined} placeholder="Ex.: Maria da Silva" />{errors.nome && <span className="field-error" id="nome-error">{errors.nome}</span>}</div>
            <div className="field"><label htmlFor="cpf">CPF</label><input id="cpf" name="cpf" inputMode="numeric" required maxLength={14} value={cpf} onChange={e => setCpf(e.target.value.replace(/[^\d.\-]/g, ''))} aria-invalid={!!errors.cpf} aria-describedby={errors.cpf ? 'cpf-help cpf-error' : 'cpf-help'} placeholder="000.000.000-00" /><span className="hint" id="cpf-help">Com ou sem pontos e traço.</span>{errors.cpf && <span className="field-error" id="cpf-error">{errors.cpf}</span>}</div>
            <div className="field"><label htmlFor="telefone">Telefone com DDD</label><input id="telefone" name="telefone" type="tel" autoComplete="tel" required maxLength={20} value={telefone} onChange={e => setTelefone(e.target.value)} aria-invalid={!!errors.telefone} aria-describedby={errors.telefone ? 'telefone-error' : undefined} placeholder="(11) 99999-9999" />{errors.telefone && <span className="field-error" id="telefone-error">{errors.telefone}</span>}</div>
            <div className="field wide"><label htmlFor="endereco">Endereço</label><input id="endereco" name="endereco" autoComplete="street-address" required maxLength={255} value={endereco} onChange={e => setEndereco(e.target.value)} aria-invalid={!!errors.endereco} aria-describedby={errors.endereco ? 'endereco-error' : undefined} placeholder="Rua, número, bairro e cidade" />{errors.endereco && <span className="field-error" id="endereco-error">{errors.endereco}</span>}</div>
          </div>
        </fieldset>
        <div className="form-actions"><button type="button" className="button secondary" disabled={saving} onClick={onCancel}>Cancelar</button><button type="submit" className="button" disabled={saving}>{saving ? 'Salvando…' : 'Cadastrar beneficiário'}</button></div>
      </form>
    </section>
  );
}
