<template>
  <div id="app" @click="checkFocusedMenu">
    <AboutWindow v-if="aboutWindowVisibility" @hideAboutWindow="hideAboutWindow"/>
    <MessageBox :message="message" v-if="messageBoxVisibility" @hideMessageBox="hideMessageBox"/>
    <TitleBar title="AutoCRUD" :focusedMenu="focusedMenu" @showAboutWindow="showAboutWindow" @showMessageBox="showMessageBox" @showMenuItems="showMenuItems" :menuItemsVisibility="menuItemsVisibility" :menus="menus"/>
    <router-view @showMenuItems="showMenuItems" @showMessageBox="showMessageBox"></router-view>
  </div>
</template>

<script>
  import TitleBar from './components/TitleBar'
  import MessageBox from './components/MessageBox'
  import AboutWindow from './components/AboutWindow'

  export default {
    name: 'autocrud',
    components: {
      TitleBar, MessageBox, AboutWindow
    },
    data() {
      return {
        focusedMenu: 0,
        menuItemsVisibility: false,
        messageBoxVisibility: false,
        aboutWindowVisibility: false,
        message: '',
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
      };
    },
    methods: {
      checkFocusedMenu() {
        this.focusedMenu = 0
      },
      showMessageBox(title, message) {
        this.messageBoxVisibility = true
        this.message = message
      },
      hideMessageBox() {
        this.messageBoxVisibility = false
      },
      showAboutWindow() {
        this.aboutWindowVisibility = true
      },
      hideAboutWindow() {
        this.aboutWindowVisibility = false
      },
      showMenuItems(bool) {
        this.menuItemsVisibility = bool
      }
    }
  }
</script>

<style>
#app {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
  /* CSS */
</style>
