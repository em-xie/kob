import { login,getInfo} from '@/api/login'
import { getToken, setToken,removeToken} from '@/utils/auth'
import { defineStore } from 'pinia'

export const useUserStore = defineStore(
    'user',
    {
      state: () => ({
        token: getToken(),
        id: "",
        username: "",
        password: "",
        photo: "",
        //token: "",
        is_login: false,
        pulling_info: true, //是否正在拉取信息
      }),
          //修改数据
    
    // mutations: {
    //     updateUser(state, user) {
    //         state.id = user.id;
    //         state.username = user.username;
    //         state.photo = user.photo;
    //         state.is_login = user.is_login;
            
    //     },
    //     // updateToken(state, token) {
    //     //     state.token = token;
    //     // },
    //     updatePullingInfo(state, pulling_info) {
    //         state.pulling_info = pulling_info;
    //     },
    //     logout(state) {
    //         state.id = "";
    //         state.username = "";
    //         state.photo = "";
    //         state.token = "";
    //         state.is_login = false;
    //     },
    // },
    actions: {
       updateUser(user) {
            this.id = user.id;
            this.username = user.username;
            this.photo = user.photo;
            //this.is_login = user.is_login;
       },
        updatePullingInfo(pulling_info) {
            this.pulling_info = pulling_info;
        },
        logout() {
            this.id = "";
            this.username = "";
            this.photo = "";
            this.token = "";
            this.is_login = false;
        },
        // 登录
        login(userInfo) {
          const username = userInfo.username.trim()
          const password = userInfo.password
          return new Promise((resolve, reject) => {
            login(username, password).then(res => {
              setToken(res.data.token.token)
              this.token = res.data.token.token
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
        },
         // 获取用户信息
      getInfo() {
        return new Promise((resolve, reject) => {
          getInfo().then(res => {
            const user = res.data.user        
            this.updateUser(user);
            this.is_login = true,
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        })
      },
        logOut() {
            removeToken();
            this.logout();
        }
        
    }}
)

