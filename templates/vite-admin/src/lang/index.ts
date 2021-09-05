import { createI18n } from 'vue-i18n'
import ElementLocale from 'element-plus/lib/locale'
import enLocale from 'element-plus/lib/locale/lang/en'
import zhLocale from 'element-plus/lib/locale/lang/zh-cn'

const messages = {
    [enLocale.name]: {
        el: enLocale.el
    },
    [zhLocale.name]: {
        el: zhLocale.el
    }
}

const i18n = createI18n({
    locale: zhLocale.name,
    fallbackLocale: enLocale.name,
    messages
})
// ElementLocale.i18n(i18n.global.t)

export default i18n