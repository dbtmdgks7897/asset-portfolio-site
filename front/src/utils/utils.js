import {reactive} from 'vue'

export const util = reactive({
    truncateText(text, maxLength) {
        if (text.length > maxLength) {
          return text.substring(0, maxLength) + "...";
        }
        return text;
      },
})



