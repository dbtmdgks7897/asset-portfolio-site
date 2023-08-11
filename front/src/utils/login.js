import {reactive} from 'vue'
import axios from 'axios';

export const login = reactive({
    isLogined: false,
    idx: null,
    nickname: null,
    img: null,
    roles: null,

    getUserProfile() {
        axios
        .get('/api/v1/auth/my')
        .then((res) => {
            this.isLogined = true;
            this.idx = res.data.data.idx;
            this.nickname = res.data.data.nickname;
            if(res.data.data.profileImg != null){
                this.img = res.data.data.profileImg;
            } else {
                this.img = require(`@/assets/img/anonymous.png`)
            }
            this.roles = res.data.data.roleList;
            return true;
        }).catch(() => {
            return false;
        })
    }
})