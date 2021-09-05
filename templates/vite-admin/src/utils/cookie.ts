import Cookies from 'js-cookie'

const DOMAIN = '.vancone.com'

export const clearAllCookies = () => {
    const cookieJson = Cookies.getJSON()
    const options = {
        domain: DOMAIN
    }
    Object.keys(cookieJson).forEach((key) => {
        Cookies.remove(key, options)
    })
}