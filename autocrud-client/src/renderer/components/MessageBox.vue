<template>
  <div id="message_box_wrapper">
    <div id="message_box">
      <TitleBar title="AutoCRUD" CloseButtonOnly="true"/>
      
      <div class="buttons">
        <p>{{message}}</p>
        <button @click="handleOK">OK</button>
      </div>
    </div>
  </div>
</template>

<script>
  const {ipcRenderer: ipc} = require('electron');
  import TitleBar from './TitleBar'
  export default {
    name: 'title-bar',
    components: {
      TitleBar
    },
    props: [
      'focusedMenu',
      'showMenuItems',
      'message'
    ],
    data() {
      return {
        positionData: 0,
        // 90, 130, 175
        menus: [
          {
            text: "File",
            offset: 90,
            focused: false,
            submenu: [
              {
                text: "New"
              },
              {
                text: "Open"
              }
            ]
          },
          {
            text: "Edit",
            offset: 130,
            focused: false,
            submenu: [
              {
                text: "Undo"
              },
              {
                text: "Redo"
              },
              {
                text: "Cut"
              },
              {
                text: "Copy"
              }
            ]
          },
          {
            text: "Help",
            offset: 175,
            focused: false,
            submenu: [
              {
                text: "Getting Started"
              },
              {
                text: "Register"
              },
              {
                text: "Check for Updates"
              },
              {
                text: "About"
              }
            ]
          }
        ],
        currentMenuItems: []
      }
    },
    methods: {
      handleOK() {
        this.$emit('hideMessageBox')
      },
      open (link) {
        this.$electron.shell.openExternal(link)
      },
      minimizeWindow() {
        // var ipc = require('electron').ipcRenderer
        ipc.send("window-min");
      },
      maxmizeWindow() {
        ipc.send("window-max");
      },
      closeWindow() {
        // alert('')
        ipc.send("window-close");
      },
      openMenu(id) {
        //this.focusedMenu = id
        //alert(id)
        this.showMenuItems = true
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
        switch(operation) {
          case "Open":
            this.openProject(); break;
          default:
            const electron = require("electron");
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
            })
            
        }
        
      },
      openProject() {
        ipc.send('show-open-dialog', 'Open AutoCRUD Project')
        ipc.once('open-finished', (event, arg) => {
          alert(arg)
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

<style scoped>
  @import url('https://fonts.googleapis.com/css?family=Source+Sans+Pro');

  * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
  }

  body { font-family: 'Source Sans Pro', sans-serif; }

  #message_box {
    background: #3c3f41;
    height: 120px;
    width: 400px;
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    margin: auto;
    box-shadow: 0px 0px 5px rgba(0,0,0,0.5);
    /* z-index: 10000; */
    /* text-align: center; */
  }

  #message_box_wrapper {
    /* background:
      radial-gradient(
        ellipse at top left,
        rgba(255, 255, 255, 1) 40%,
        rgba(229, 229, 229, .9) 100%
      ); */
    position: fixed;
    left: 0;
    top: 0;
    height: 100%;
    width: 100%;
  }

  /* h1 {
    color: #bbb;
    font-weight: normal;
    font-size: 14px;
    display: inline-block;
  } */

  .buttons {
    width: 100%;
    height: 50px;
    text-align: center;
  }

  button {
    background: #365880;
    border: solid 1px #4c708c;
    border-radius: 5px;
    outline: none;
    color: #bbb;
    font-weight: 500;
    width: 80px;
    height: 25px;
  }

  p {
    color: #bbb;
    margin-top: 10px;
    margin-bottom: 10px;
    height: calc(100% - 20px);
  }
  
</style>
