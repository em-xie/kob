<template>
    <ContentField>
        <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>玩家</th>
                    <th>天梯分</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>
                        <img :src="user.photo" alt="" class="record-user-photo">
                        &nbsp;
                        <span class="record-user-username">{{ user.username }}</span>
                    </td>
                    <td>{{ user.rating }}</td>
                </tr>
            </tbody>
        </table>
        <!-- <SoftPagination>
            <SoftPaginationItem :label="前一页" @click="click_page(-2)">
                
            </SoftPaginationItem>
            <SoftPaginationItem>

            </SoftPaginationItem>
            <SoftPaginationItem>

            </SoftPaginationItem>
        </SoftPagination> -->
        

        <nav aria-label="...">
        <ul class="pagination" style="float: right;">
            <li class="page-item" @click="click_page(-2)">
                <a class="page-link" href="#">前一页</a>
            </li>
            <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
                <a class="page-link" href="#">{{ page.number }}</a>
            </li>
            <li class="page-item" @click="click_page(-1)">
                <a class="page-link" href="#">后一页</a>
            </li>
        </ul>
        </nav>
    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import {useUserStore} from '@/store/modules/user'
// import SoftPaginationItem from '../../components/SoftPaginationItem.vue'
// import SoftPagination from '../../components/SoftPagination.vue'
import { ref } from 'vue';
import {getlist} from '@/api//rankList/rankList'

export default {
    components: {
        ContentField,
        // SoftPaginationItem,
        // SoftPagination
    },
    setup() {
        // const pkStore = useStore('pk');
        // const recordStore = useStore('record');
        const userStore = useUserStore()
        let users = ref([]);
        let current_page = 1;
        let total_users = 0;
        let pages = ref([]);

        const click_page = page => {
            if (page === -2) page = current_page - 1;
            else if (page === -1) page = current_page + 1;
            let max_pages = parseInt(Math.ceil(total_users / 10));

            if (page >= 1 && page <= max_pages) {
                pull_page(page);
            }
        }

        const udpate_pages = () => {
            let max_pages = parseInt(Math.ceil(total_users / 10));
            let new_pages = [];
            for (let i = current_page - 2; i <= current_page + 2; i ++ ) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === current_page ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }

        const pull_page = page => {
            current_page = page;
            return new Promise((resolve, reject) => {
                //console.log(page)
                getlist(page).then(res => {
                    users.value = res.users;
                    //console.log(users.value);
                    total_users = res.users_count;
                    udpate_pages();
                    
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
           
        }

        pull_page(current_page);

        return {
            users,
            pages,
            click_page,      
            // pkStore,
             userStore,
            // recordStore
        
        }
    }
}
</script>

<style scoped>
img.record-user-photo {
    width: 4vh;
    border-radius: 50%;
}
</style>
