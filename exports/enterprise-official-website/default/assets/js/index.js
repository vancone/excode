var arrLang = {
  "en": {
    "HOME": "HOME",
    "ABOUT": "ABOUT",
    "CONTACT": "CONTACT",
    "SIGN_UP": "Sign up",
    "OR": "or",
    "LOG_IN": "Log in",
    "TTMEK": "MekCone",
    "WE_BUILD_WEBSITES": "We build websites.",
    "WE_ARE_": "We are prepared to be a professional IT solution provider.",
    "A_PROFESSIONAL_": "A professional IT solution provider",
    "SIGN_UP_FOR_FREE": "SIGN UP FOR FREE",
  },

  "zh": {
    "HOME": "首页",
    "ABOUT": "关于",
    "CONTACT": "联络",
    "SIGN_UP": "注册",
    "OR": "或",
    "LOG_IN": "登录",
    "TTMEK": "甜筒云",
    "WE_BUILD_WEBSITES": "我们专业建站",
    "WE_ARE_": "我们准备好成为专业的IT解决方案提供商",
    "A_PROFESSIONAL_": "专业的IT解决方案提供商",
    "SIGN_UP_FOR_FREE": "免费注册",
  },

  "th": {
    "HOME": "หน้าแรก",
    "ABOUT": "เกี่ยวกับ",
    "CONTACT": "ติดต่อ",
    "SIGN_UP": "ลงชื่อ",
    "OR": "หรือ",
    "LOG_IN": "เข้าสู่ระบบ",
    "TTMEK": "เมฆโคน.com",
    "WE_BUILD_WEBSITES": "เราสร้างเว็บไซต์",
    "WE_ARE_": "เราพร้อมที่จะเป็นผู้ให้บริการโซลูชันไอทีอย่างมืออาชีพ",
    "A_PROFESSIONAL_": "ผู้ให้บริการโซลูชันไอทีอย่างมืออาชีพ",
    "SIGN_UP_FOR_FREE": "ลงทะเบียนฟรี",
  }
};

function getQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  } else {
    return null;
  }
}


$(document).ready(function() {
  // The default language is English
  var lang = "en";
  // Check for localStorage support
  var queryString = getQueryString('lang')
  if (queryString != null && queryString != "") {
    lang = queryString
  } else if('localStorage' in window){
    var lang = localStorage.getItem('lang') || navigator.language.slice(0, 2);
    if (!Object.keys(arrLang).includes(lang)) lang = 'en';
  }

  $(".lang").each(function(index, element) {
    $(this).text(arrLang[lang][$(this).attr("key")]);
  });

  // get/set the selected language
  $(".translate").click(function() {
    var lang = $(this).attr("id");
    $("#lang_label").text(lang.toUpperCase())

    // update localStorage key
    if('localStorage' in window){
      localStorage.setItem('lang', lang);
      console.log( localStorage.getItem('lang') );
    }

    $(".lang").each(function(index, element) {
      $(this).text(arrLang[lang][$(this).attr("key")]);
    });
  });
});



/* var lang;

function ajax(method, url, param) {
  var xmlhttp, res;
  if (window.XMLHttpRequest) {
    xmlhttp = new XMLHttpRequest();
  } else {
    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }

  xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
      res = xmlhttp.responseText;
    }
  };
  if (method == "GET") {
    xmlhttp.open(method, url + param, false);
    xmlhttp.send();
  } else {
    xmlhttp.open(method, url, false);
    xmlhttp.setRequestHeader(
      "Content-Type",
      "application/x-www-form-urlencoded;"
    );
    xmlhttp.send(param);
  }
  return res;
}

function getQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  } else {
    return null;
  }
}

function getCookie(name) {
  var arr,
    reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
  if ((arr = document.cookie.match(reg))) return unescape(arr[2]);
  else return null;
}

function initial() {
    var username = localStorage.getItem("username");
    if (username != null) {
      document.getElementById("account").innerHTML =
        '<a href="http://www.gakiton.cn/account">' +
        username +
        '</a>&nbsp;&nbsp;&nbsp;<a href="http://www.gakiton.cn/account/#/register">' +
        "退出登录" +
        "</a>";
    }

  // var lang = navigator.language;

  // var param_lang = getQueryString("lang");
  // if (param_lang != null) {
  //   if (param_lang == "zh-cn") {
  //     lang = JSON.parse(ajax("GET", "assets/lang/zh-cn.json", ""));
  //   }
  // } else {
  //   if (lang == "zh-CN") {
  //     lang = JSON.parse(ajax("GET", "assets/lang/zh-cn.json", ""));
  //   } else {
  //     lang = JSON.parse(ajax("GET", "assets/lang/en-us.json", ""));
  //   }
  // }

  // document.title = lang.title;
  // document.getElementById("header_title").innerText = lang.header_title;
  // document.getElementById("button_login").innerText = lang.button_login;
  // document.getElementById("button_register").innerText = lang.button_register;
  // document.getElementById("myName").innerText = lang.myName;

  // var id = localStorage.getItem("id");
  // var nickname = localStorage.getItem("nickname");

  // if (id == "" || id == null) {
  //   document.getElementById("button_login").innerText = lang.button_login;
  //   document.getElementById("button_login").style.display = "inline-block";
  // } else {
  //   // alert(id)
  //   if (nickname != null) {
  //     document.getElementById("button_login").innerText = nickname;
  //     document.getElementById("button_login").style.display = "inline-block";
  //   }
  //   // document.getElementById('button_login').style.display ='none'
  // }
}
 */