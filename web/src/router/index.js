import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RanklistIndexView from '../views/ranklist/RanklistIndexView'
import ExternIndexView from '../views/extern/ExternIndexView'
import RecordIndexView from '../views/record/RecordIndexView'
import RecordContentView from '../views/record/RecordContentView'
import UserBotIndexView from '../views/user/bot/UserBotIndexView'
import NotFound from '../views/error/NotFound'
import PictureIndexView from '../views/picture/PictureIndexView.vue'
import PictureDownload from '../views/picture/PictureDownload.vue'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegister from '../views/user/account/UserAccountRegisterView.vue'
import UserAccountAcWingWebReceiveCodeView from '../views/user/account/UserAccountAcWingWebReceiveCodeView'
import { useUserStore } from '@/store/modules/user'
const routes = [  
  {
    // 重定向到pk页面
    path: "/",
    name: "home",
    redirect: "/extern/",
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/record/:recordId/",
    name: "record_content",
    component: RecordContentView,
    meta: {
        requestAuth: true,
    }
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/extern/",
    name: "extern_index",
    component: ExternIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/bot",
    name: "user_bot_index",
    component: UserBotIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegister,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/user/account/acwing/web/receive_code/",
    name: "user_account_acwing_web_receive_code",
    component: UserAccountAcWingWebReceiveCodeView,
    meta: {
        requestAuth: false,
    }
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
    meta: {
      requestAuth: false,
    }
  },
  {
    path: "/picture/",
    name: "picture_index",
    component: PictureIndexView,
    meta: {
      requestAuth: true,
    }
  },
  {
    path: "/picturedownload/",
    name: "picturedownload_index",
    component: PictureDownload,
    meta: {
      requestAuth: true,
    }
  },
  
]



const router = createRouter({
  history: createWebHistory(),
  routes
})

// to跳转到哪个页面， from表示从哪个页面跳转过去
// next的表示将页面要不要执行下一步操作，写之前首先要记录每一个未授权界面
router.beforeEach((to, from, next) => {
  const store = useUserStore()
  
  //console.log(store.is_login)
  if (to.meta.requestAuth && !store.is_login) {
    next({name: "user_account_login"})
  } else {
    next();
  }
})
// router.beforeEach((to, from, next) => {
//   const jwt_token = localStorage.getItem("jwt_token");

//   let st = true;
//   if (jwt_token) {
//     store.commit("updateToken", jwt_token);
//     store.dispatch("getinfo", {
//       success() {},
//       error() {
//         router.push({ name: "user_account_login" });
//       },
//     });
//   } else {
//     st = false;
//   }

//   if (to.meta.requestAuth && !store.state.user.is_login && !st) {
//     next({ name: "user_account_login" });
//   } else {
//     next();
//   }
// });

export default router
