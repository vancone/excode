let project = {}
let extensions = []
let commonMessage = ''

export default {
  project,
  extensions,
  getCommonMessage: function () {
    return commonMessage
  },
  setCommonMessage: function (msg) {
    commonMessage = msg
  },
  getUrlKey: function (name) {
    return decodeURIComponent(
      (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ''])[1].replace(/\+/g, '%20')) || null
  }
}
