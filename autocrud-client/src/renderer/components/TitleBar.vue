<template>
  <div id="topbar_wrapper" style="-webkit-app-region:drag;">
    <div class="main_wrapper">
    <h1>{{title}}</h1>

    <div class="MenuBar" v-if="menus != null">
      <li v-for="(item, index) in menus" :key="item.text" :class="{FocusedMenu: item.focused}" @click="openMenu(index)">{{item.text}}</li>
      <div class="MenuItems" :style="positionData" v-if="menuItemsVisibility">
        <li v-for="item in currentMenuItems" :key="item.text" @click="handleMenu(item.text)">{{item.text}}</li>
      </div>
    </div>
    </div>

    <div class="ButtonsWrapper">
      <div class="ButtonWrapper" id="ButtonMinWrapper" v-show="CloseButtonOnly!='true'">
        <img class="Button" @click="minimizeWindow" src="~@/assets/min.svg"/>
      </div>
      <div class="ButtonWrapper" id="ButtonMaxWrapper" v-show="CloseButtonOnly!='true'">
        <img class="Button" @click="maximizeWindow" src="~@/assets/max.svg"/>
      </div>
      <div class="ButtonWrapper" id="ButtonCloseWrapper">
        <img class="Button" @click="closeWindow" src="~@/assets/close.svg"/>
      </div>
    </div>
  </div>
</template>

<script>
  const {ipcRenderer: ipc} = require('electron');
  export default {
    name: 'title-bar',
    props: [
      'focusedMenu',
      'menuItemsVisibility',
      'title',
      'menus',
      'CloseButtonOnly'
    ],
    data() {
      return {
        positionData: 0,
        //menuItemsVisibilityValue: this.menuItemsVisibility,
        currentMenuItems: []
      }
    },
    methods: {
      open (link) {
        this.$electron.shell.openExternal(link)
      },
      minimizeWindow() {
        // var ipc = require('electron').ipcRenderer
        ipc.send("window-min");
      },
      maximizeWindow() {
        ipc.send("window-max");
      },
      closeWindow() {
        ipc.send("window-close");
      },
      openMenu(id) {
        this.$emit('showMenuItems', true)
        this.positionData = "left:" + this.menus[id].offset + "px"
        this.currentMenuItems = this.menus[id].submenu
        for(var i = 0; i < this.menus.length; i ++) {
          if (i == id) {
            this.menus[id].focused = true
          } else {
            this.menus[i].focused = false
          }
        }
        /* switch(id) {
           case 1: 
            this.currentMenuItems = this.fileMenuItems; 
            this.positionData = 'left: 90px'
            //document.getElementById("MenuItems").style.background="#f00"
            break;

          case 2: 
            this.currentMenuItems = this.editMenuItems; 
            this.positionData = 'left: 135px'
            //document.getElementById("MenuItems").style.background="#f00"
            break;
        } */
      },
      handleMenu(operation) {
        this.$emit('showMenuItems', false)
        switch(operation) {
          case "Open":
            this.openProject(); break;
          case "About":
            this.$emit('showAboutWindow'); break;
          default:
            this.$emit('showMessageBox', 'title', operation)
            /* const electron = require("electron");
             let BrowserWindow = electron.remote.BrowserWindow;
             const messageBoxURL = process.env.NODE_ENV === 'development'
	              ? `http://localhost:9080`
                : `file://${__dirname}/MessageBox.html`
                alert(messageBoxURL)
            let messageBoxWindow = new BrowserWindow({
              width: 1000, 
              height: 500,
              frame: false,
              movable: false
            })
            messageBoxWindow.loadURL("http://localhost:9080/MessageBox.html")
            messageBoxWindow.once("close", function() {
              messageBoxWindow = null
            }) */
            
        }
        
      },
      openProject() {
        var that = this
        ipc.send('show-open-dialog', 'Open AutoCRUD Project')
        ipc.once('open-finished', (event, arg) => {
          if (arg != '') {
            const fs = require("fs");
            fs.readFile(arg.toString(), "utf-8", function(error, data) {
              if (error) return console.log("读取文件失败,内容是" + error.message);
              that.$emit('showMessageBox', 'AutoCRUD', data)
            });
            
          }
          
        })
        
      }
    },
    watch: {
    /* "focusedMenu": function(val, oldVal) {
      // alert(val)
      if (val > 0) {
        for(var i = 1; i < 3; i ++) {
          document.getElementById("menu" + i).style.display = 'none'
        }
        document.getElementById("menu" + val).style.display = 'inline-block'
        // alert(val)
      } else {
        document.getElementById("menu" + val).style.display = 'none'
      }
    } */
  }
  }

</script>

<style>
  @import url('https://fonts.googleapis.com/css?family=Source+Sans+Pro');

  * {
    /* box-sizing: border-box; */
    margin: 0;
    padding: 0;
  }

  body { font-family: 'Source Sans Pro', sans-serif; }

  #topbar_wrapper {
    /* background:
      radial-gradient(
        ellipse at top left,
        rgba(255, 255, 255, 1) 40%,
        rgba(229, 229, 229, .9) 100%
      ); */
    background: #3c3f41;
    border-bottom: solid 1px #666;
    height: 30px;
    width: inherit;
    -webkit-app-region: no-drag;
    -webkit-user-select: none;
  }

  .main_wrapper {
    width: calc(100% - 149px);
    display: inline-block;
    margin: 0;
    height: 30px;
    line-height: 30px;
    vertical-align: top;
  }

  h1 {
    color: #bbb;
    font-weight: normal;
    font-size: 14px;
    display: inline-block;
    line-height: 30px;
    height: 30px;
    margin: 0;
    margin-left: 8px;
  }

  .MenuBar {
    display: inline-block;
    margin: 0;
    margin-left: 20px;
    line-height: 30px;
    height: 30px;
    -webkit-app-region: no-drag;
  }

  .MenuBar li {
    list-style-type: none;
    color: #bbb;
    display: inline-block;
    margin: 0;
    padding-left: 10px;
    padding-right: 10px;
    height: 30px;
    line-height: 30px;
    font-size: 14px;
  }

  .MenuBar li:hover {
    background: #4b6eaf;
  }

  .MenuItems {
    position: fixed;
    top: 30px;
    left: 0;
    width: 200px;
    border: solid 1px #666;
    /* display: none; */
    background: #3c3f41;
  }

  .FocusedMenu {
    background: #4b6eaf;
  }

  .MenuItems li {
    height: 25px;
    padding-left: 30px;
    display: block;
    line-height: 25px;
  }

  .ButtonsWrapper {
    display: inline-block;
    width: 144px;
    height: 30px;
    text-align: right;
  }

  .ButtonWrapper {
    height: 30px;
    width: 45px;
    display: inline-block;
    text-align: center;
  }



  #ButtonMinWrapper:hover {
    background: #4f5254;
  }

  #ButtonMaxWrapper:hover {
    background: #4f5254;
  }

  .ButtonWrapper:hover {
    background: #e81123;
  }

  .Button {
    height: 30px;
    width: 30px;
    -webkit-app-region: no-drag;
  }
</style>
