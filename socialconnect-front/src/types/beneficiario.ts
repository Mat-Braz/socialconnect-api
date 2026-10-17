export interface Beneficiario {
  idBeneficiario: number;
  nome: string;
  cpf: string;
  telefone: string | null;
  endereco: string | null;
  situacaoVulnerabilidade: string | null;
  dataCadastro: string;
}

export interface BeneficiarioRequest {
  nome: string;
  cpf: string;
  telefone: string;
  endereco: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface ApiProblem {
  title: string;
  detail: string;
  status: number;
  errors?: { field: string; message: string }[];
}
