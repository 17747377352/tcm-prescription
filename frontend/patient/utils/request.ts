import axios from 'axios'

const client = axios.create({
  baseURL: '/tcm-api',
  timeout: 30000
})

export const request = <T = any>(options: { url: string; method?: string; data?: any; params?: any }) => {
  return client.request<T>({
    url: options.url,
    method: (options.method || 'GET') as any,
    data: options.data,
    params: options.params
  }).then(res => res.data as any)
}
