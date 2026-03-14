const r="http://localhost:8080/api",c=s=>new Promise((a,t)=>{uni.request({...s,url:`${r}${s.url}`,success:o=>{const e=o.data;e&&(e.code===0||e.code===void 0)?a(e):t(e)},fail:t})});export{c as r};
