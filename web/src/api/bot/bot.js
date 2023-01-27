import request from '@/utils/request'

// 删除bot
export function removeBot(data) {

  return request({
    url: '/user/bot/remove/',
    headers: {
      isToken: true
    },
    method: 'post',
    data: data
  })
}

//增加bot
export function addBot(data) {
    return request({
      url: '/user/bot/add/',
      headers: {
        isToken: true
      },
      method: 'post',
      data: data
    })
}


export function BotContextExample() {
  return request({
    url: '/bot/context/example/',
    headers: {
      isToken: true
    },
    method: 'post',
    
  })
}

export function updateBot(data) {
    return request({
      url: '/user/bot/update/',
      headers: {
        isToken: true
      },
      method: 'post',
      data: data
    })
  }
  

  // 获取bot列表
  export function getlist() {
    return request({
      url: '/user/bot/getlist/',
      method: 'get'
    })
  }
  
//   // 退出方法
//   export function logout() {
//     return request({
//       url: '/logout',
//       method: 'post'
//     })
//   }
  