import {reactive} from 'vue'

export const imgTrans = reactive({
    decode(img, type) {
        return `data:image/${type};base64,`+img;
    }
})