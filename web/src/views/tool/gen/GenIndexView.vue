<template>
    <ContentField>
        <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th>序号</th>
                    <th>表名称</th>
                    <th>表描述</th>
                    <th>实体</th>
                    <th>创建时间</th>
                    <th>更新时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="table in tableList" :key="table.index">
                    <td>
                        {{table.tableId}}
                    </td>
                    <td>
                        {{table.tableName}}
                    </td>
                    <td>{{table.tableComment}}</td>
                    <td>{{ table.className }}</td>
                    <td>{{ table.createTime }}</td>
                    <td>{{ table.updateTime }}</td>
                    <td>
                        <button @click="updateTable(table)" type="button" class="btn btn-secondary">生成</button>
                        <button @click="deleteTable(table)" type="button" class="btn btn-secondary">删除</button>
                    </td>
                </tr>
            </tbody>
        </table>
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
        <button  type="button" class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#add-db-btn">导入表</button>
                        <div class="modal fade" id="add-db-btn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLabel">导入表</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
            <table class="table table-striped table-hover" style="text-align: center;">
            <thead>
                <tr>
                    <th></th>
                    <th>表名称</th>
                    <th>表描述</th>
                    <th>创建时间</th>
                    <th>更新时间</th>
                </tr>
            </thead>
            <tbody ref="table"> 
            <tr v-for="db in dbList" :key="db.index" >
                
            <td >
                    <div class="form-check" style="margin-left: 60%;" >
                        <input class="form-check-input" type="checkbox" id="flexCheckDefault" :value="db.tableName" @click="handleSelectionChange($event) ">
                    </div>
               
            </td>
            
                    <td>
                        {{db.tableName}}
                    </td>
                    <td>{{db.tableComment}}</td>
                    
                    <td>{{ db.createTime }}</td>
                    <td>{{ db.updateTime }}</td>
                </tr>
            
            </tbody>
        
        </table>
        <nav aria-label="...">
        <ul class="pagination" style="float: right;">
            <li class="page-item" @click="click_dbpage(-2)">
                <a class="page-link" href="#">前一页</a>
            </li>
            <li :class="'page-item ' + dbpage.is_active" v-for="dbpage in dbpages" :key="dbpage.number" @click="click_dbpage(dbpage.number)">
                <a class="page-link" href="#">{{ dbpage.number }}</a>
            </li>
            <li class="page-item" @click="click_dbpage(-1)">
                <a class="page-link" href="#">后一页</a>
            </li>
        </ul>
        </nav>
        </div>
                                <div class="modal-footer">
                                    <div class="error-message">{{ dbadd.error_message }}</div>
                                    <button type="button" class="btn btn-primary" @click="add_db">导入</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                </div>
                                </div>
                            </div>
                        </div>
    </ContentField>
</template>

<script>

import ContentField from '@/components/ContentField.vue'
import { ref,reactive,toRefs,getCurrentInstance } from 'vue';
import {listDbTable,listTable,importTable,genCode,delTable} from '@/api/tool/generator/gen'
import { Modal } from 'bootstrap/dist/js/bootstrap'

export default {
    components: {
        ContentField,
    },

    setup() {
        const { proxy } = getCurrentInstance();

        const tables = ref([]);
        const tableList = ref([]);
        const total = ref(0);
        const data = reactive({
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                dataName: "master"
            }
        });

        const dbList = ref([]);
        const dbtotal = ref(0);
        const dbdata = reactive({
            // 查询参数
            dbqueryParams: {
                pageNum: 1,
                pageSize: 10,
                dataName: "master"
            }
        });
        const dbadd = reactive({
            
            error_message: "",
        })
const { queryParams} = toRefs(data);
const { dbqueryParams} = toRefs(dbdata);
localStorage.setItem("dataName", queryParams.value.dataName);
let pages = ref([]);
        const click_page = page => {
            if (page === -2) page = queryParams.value.pageNum - 1;
            else if (page === -1) page = queryParams.value.pageNum + 1;
            let max_pages = parseInt(Math.ceil(total.value/ 10));
            if (page >= 1 && page <= max_pages) {   
                pull_page(page);
            }
        }

        const udpate_pages = () => {
            let max_pages = parseInt(Math.ceil(total.value / 10));
            let new_pages = [];
            for (let i = queryParams.value.pageNum - 2; i <= queryParams.value.pageNum + 2; i ++ ) {
                if (i >= 1 && i <= max_pages) {
                    new_pages.push({
                        number: i,
                        is_active: i === queryParams.value.pageNum ? "active" : "",
                    });
                }
            }
            pages.value = new_pages;
        }

        const pull_page = page => {
            queryParams.value.pageNum = page;
            return new Promise((resolve, reject) => {
                listTable( queryParams.value).then(res => {
                    tableList.value = res.rows;
                    total.value = res.total;
                    udpate_pages();
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
        }

        pull_page(queryParams.value.pageNum);
        function updateTable(table){
            const tbNames = table.tableName ;
            if (tbNames == "") {
                
                return;
            }
            if (table.genType === "1") {
                genCode(table.tableName)
            } else {
                proxy.$download.zip("/tool/gen/batchGenCode?tables=" + tbNames, "gen.zip");
            }
        }

        function deleteTable(table){
            const tableIds = table.tableId;
             delTable(tableIds).then(response => {
                if(response.code === 200)
                pull_page(queryParams.value.pageNum);
            })
          
        }
const tableNameArray = []

/** 多选框选中数据 */
function handleSelectionChange(e) {
    let checked =  e.target.checked
    let j = 0;
    let checkDom = proxy.$refs.table.getElementsByClassName("form-check-input")
   // console.log(checked)
    if(checked){
        for(var i = j; i < checkDom.length; i++){
            if(checkDom[i].checked ){
                tableNameArray.push(checkDom[i].value)
              // console.log(tableNameArray)
                j = checkDom.length - i;
            }else{
                j = 0;
            }
        }
    }else{
        tableNameArray.pop(i);
    }
    
    

    
}

        //创建一个 bot
        const add_db = () => {
            dbadd.error_message = "";
            const tableNames = tableNameArray.join(",");
            
            if (tableNames == "") {
                dbadd.error_message = "请选择要导入的表";
            return;
            }
            return new Promise((resolve, reject) => {
                importTable( {tableNameArray: tableNames}).then(res => {
                if (res.code === 200) {
                                
                                Modal.getInstance("#add-db-btn").hide();
                                pull_page(queryParams.value.pageNum);
                            } else {
                                dbadd.error_message = "导入失败";
                            }
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
        }

        let dbpages = ref([]);
        const click_dbpage = dbpage => {
            
            if (dbpage === -2) dbpage = dbqueryParams.value.pageNum - 1;
            else if (dbpage === -1) dbpage = dbqueryParams.value.pageNum + 1;
            
            let max_dbpages = parseInt(Math.ceil(dbtotal.value/ 10));
            
            if (dbpage >= 1 && dbpage <= max_dbpages) {
                
                pull_page(dbpage);
            }
        }

        const udpate_dbpages = () => {
            let max_dbpages = parseInt(Math.ceil(dbtotal.value / 10));
            let new_dbpages = [];
            for (let i = dbqueryParams.value.pageNum - 2; i <= dbqueryParams.value.pageNum + 2; i ++ ) {
                if (i >= 1 && i <= max_dbpages) {
                    new_dbpages.push({
                        number: i,
                        is_active: i === dbqueryParams.value.pageNum ? "active" : "",
                    });
                }
            }
            dbpages.value = new_dbpages;
        }

        const pull_dbpage = dbpage => {
            dbqueryParams.value.pageNum = dbpage;
            
            return new Promise((resolve, reject) => {
                
                listDbTable( dbqueryParams.value).then(res => {
                    dbList.value = res.rows;
                    dbtotal.value = res.total;
                    udpate_dbpages();
              resolve()
            }).catch(error => {
              reject(error)
            })
          })
        }

        pull_dbpage(dbqueryParams.value.pageNum);

        return {
            
            tableNameArray,
            handleSelectionChange,
            tables,
            
            tableList,
            dbList,
            dbtotal,
            dbdata,
            pages,
            dbpages,
            click_page,
            queryParams,
            dbqueryParams,
            total,
            updateTable,
            deleteTable,
            add_db,
            dbadd,
            click_dbpage,
            

        }
    }
}
</script>

<style scoped>
div.playground {
    /* 浏览器的宽高 */
    width: 60vw;
    height: 70vh;
    /* background: lightblue; */


    /* 居中 */
    margin: 40px auto;
}
</style>