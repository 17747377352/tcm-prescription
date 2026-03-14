const BASE_URL = 'http://localhost:8080/api'

export const request = <T = any>(options: UniApp.RequestOptions) => {
  return new Promise<T>((resolve, reject) => {
    uni.request({
      ...options,
      url: `${BASE_URL}${options.url}`,
      success: (res) => {
        const data: any = res.data
        if (data && (data.code === 0 || data.code === undefined)) {
          resolve(data)
        } else {
          reject(data)
        }
      },
      fail: reject
    })
  })
}
