import axios from 'axios';
import type { ApiProblem } from '../types/beneficiario';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' },
});

export function describeApiError(error: unknown): string {
  if (axios.isAxiosError<ApiProblem>(error)) {
    if (!error.response) return 'Não foi possível acessar a API. Verifique sua conexão e se o backend está em execução.';
    if (error.response.status === 409) return 'Este CPF já está cadastrado. Confira os dados informados.';
    const problem = error.response.data;
    if (problem.errors?.length) return problem.errors.map(item => item.message).join(' ');
    return problem.detail || 'Não foi possível concluir a solicitação. Tente novamente.';
  }
  return 'Ocorreu um erro inesperado. Tente novamente.';
}

export default api;
