import axios from 'axios'
import { saveAs } from 'file-saver'
import { getToken } from '@/utils/auth'


const baseURL = process.env.VUE_APP_BASE_API


export default {
  oss(ossId) {
    
    var url = baseURL + '/system/oss/download/' + ossId
    
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { 'Authorization': 'Bearer ' + getToken() }
    }).then(async (res) => {
     
     
        const blob = new Blob([res.data], { type: 'application/octet-stream' })
        this.saveAs(blob, decodeURI(res.headers['download-filename']))
      
      
    }).catch((r) => {
      console.error(r)
     
      
    })
  },
  zip(url, name) {
    // eslint-disable-next-line no-redeclare
    var url = baseURL + url
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: {
        'Authorization': 'Bearer ' + getToken(),
        'datasource': localStorage.getItem("dataName")
      }
    }).then(async (res) => {
        const blob = new Blob([res.data], { type: 'application/zip' })
        this.saveAs(blob, name)
      
    })
  },
  saveAs(text, name, opts) {
    saveAs(text, name, opts);
  },
  
}

