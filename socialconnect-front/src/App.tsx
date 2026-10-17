import { useEffect, useRef, useState } from 'react';
import { BeneficiarioList } from './components/BeneficiarioList';
import { BeneficiarioForm } from './components/BeneficiarioForm';

export default function App() {
  const [screen, setScreen] = useState<'list' | 'form'>('list');
  const [refreshKey, setRefreshKey] = useState(0);
  const [message, setMessage] = useState('');
  const heading = useRef<HTMLHeadingElement>(null);
  useEffect(() => { heading.current?.focus(); }, [screen]);

  const openForm = () => { setMessage(''); setScreen('form'); };
  return (
    <>
      <a className="skip-link" href="#conteudo">Pular para o conteúdo</a>
      <header className="topbar"><div className="brand"><span className="brand-mark" aria-hidden="true">sc</span><span>Social<strong>Connect</strong><small>Conexões que transformam vidas</small></span></div><span className="workspace-label">Gestão social</span></header>
      <main id="conteudo">
        <nav className="breadcrumb" aria-label="Localização"><button onClick={() => setScreen('list')}>SocialConnect</button><span aria-hidden="true">/</span><span>Beneficiários</span>{screen === 'form' && <><span aria-hidden="true">/</span><span>Novo cadastro</span></>}</nav>
        <div className="page-heading"><div><p className="eyebrow">CUIDADO QUE COMEÇA COM CONEXÃO</p><h1 ref={heading} tabIndex={-1}>{screen === 'list' ? 'Beneficiários' : 'Cadastrar beneficiário'}</h1><p>{screen === 'list' ? 'Organize os cadastros e mantenha as pessoas no centro do cuidado.' : 'Um novo cadastro para aproximar quem precisa de quem pode ajudar.'}</p></div>{screen === 'list' && <button className="button" onClick={openForm}><span aria-hidden="true">＋</span> Novo beneficiário</button>}</div>
        {message && <div className="notice success" role="status">{message}</div>}
        {screen === 'list' ? <BeneficiarioList refreshKey={refreshKey} onCreate={openForm} /> : <BeneficiarioForm onCancel={() => setScreen('list')} onSuccess={nome => { setMessage(`Beneficiário ${nome} cadastrado com sucesso!`); setRefreshKey(value => value + 1); setScreen('list'); }} />}
        <footer>SocialConnect <span aria-hidden="true">·</span> Pessoas conectadas. Comunidades fortalecidas.</footer>
      </main>
    </>
  );
}
