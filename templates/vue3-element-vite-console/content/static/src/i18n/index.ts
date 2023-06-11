import { createI18n } from 'vue-i18n';
import zh from './langs/zh';
import en from './langs/en';

const i18n = createI18n({
    locale: localStorage.getItem('locale') || navigator.language.slice(0, 2),
    messages: {
    	zh, en
	}
});

export default i18n;