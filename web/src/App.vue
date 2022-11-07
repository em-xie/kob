<template>
  <NavBar></NavBar>
  <router-view></router-view>
</template>
<script>
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap"
// import $ from 'jquery'
export default {
  components: {
    NavBar
  },
  // setup(){
    // $.ajax({
    //   url: "http://127.0.0.1:8089/user/account/token/",
    //   type: "post",
    //   data: {
    //     username: 'xie',
    //     password: '123456',
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }
    // });
  //   $.ajax({
  //     url: "http://127.0.0.1:8089/user/account/info/",
  //     type: "get",
  //     headers: {
  //       //授权 
  //       Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4MDY2ZjBhZWZlMmE0OGNhOGExYTEwOGVhOTdkMTJkYyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2NzgwMzg3NywiZXhwIjoxNjY5MDEzNDc3fQ.EtSg9dHEZKUcAd-p8-A92s24aVPQSXLZbrsIVzRkysI"
  //     },
  //     success(resp) {
  //       console.log(resp);
  //     },
  //     error(resp) {
  //       console.log(resp);
  //     }
  //   });
  //   $.ajax({
  //     url: "http://127.0.0.1:8089/user/account/register/",
  //     type: "post",
  //     data: {
  //       username: 'xie1',
  //       password: '123',
  //       confirmedPassword: '123',
  //     },
  //     success(resp) {
  //       console.log(resp);
  //     },
  //     error(resp) {
  //       console.log(resp);
  //     }
  //   });
  // }
}
</script>

<style>
body {
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
</style>
