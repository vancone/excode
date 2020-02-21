import { app, BrowserWindow } from 'electron'

/**
 * Set `__static` path to static files in production
 * https://simulatedgreg.gitbooks.io/electron-vue/content/en/using-static-assets.html
 */
if (process.env.NODE_ENV !== 'development') {
	global.__static = require('path').join(__dirname, '/static').replace(/\\/g, '\\\\')
}

let mainWindow
const winURL = process.env.NODE_ENV === 'development'
	? `http://localhost:9080`
	: `file://${__dirname}/index.html`

function createWindow () {
	/**
	 * Initial window options
	 */
	mainWindow = new BrowserWindow({
		height: 563,
		useContentSize: true,
		width: 1000,
		webPreferences: {
			nodeIntegration: true
		},
		frame: false
	})

	mainWindow.loadURL(winURL)

	mainWindow.on('closed', () => {
		mainWindow = null
	})
}

const electron = require('electron')
const ipc = electron.ipcMain

ipc.on('window-min', function () {
  mainWindow.minimize();
})

ipc.on('window-max', function () {
  if (mainWindow.isMaximized()) {
    mainWindow.restore();
  } else {
    mainWindow.maximize();
  }
})
ipc.on('window-close', function () {
  mainWindow.close();
})


const dialog = require('electron').dialog

ipc.on('show-message-box', function(event) {
	/* dialog.showMessageBox(mainWindow, {
		type: "info",
		title: 'nothing',
		message: "host"
	}) */
	if (messageBox == null) {
		messageBox = new BrowserWindow({
			height: 200,
			useContentSize: true,
			width: 500,
			webPreferences: {
				nodeIntegration: true
			},
			frame: false
		})
	
		messageBox.loadURL(messageBoxURL)
	
		messageBox.on('closed', () => {
			messageBox = null
		})
	}
  })

ipc.on('show-open-dialog', function(event, title) {
	dialog.showOpenDialog(mainWindow, {
		title: title
	}, function(filename) {
		event.sender.send("open-finished", filename)
	})
})

app.on('ready', createWindow)

app.on('window-all-closed', () => {
	if (process.platform !== 'darwin') {
		app.quit()
	}
})

app.on('activate', () => {
	if (mainWindow === null) {
		createWindow()
	}
})

/**
 * Auto Updater
 *
 * Uncomment the following code below and install `electron-updater` to
 * support auto updating. Code Signing with a valid certificate is required.
 * https://simulatedgreg.gitbooks.io/electron-vue/content/en/using-electron-builder.html#auto-updating
 */

/*
import { autoUpdater } from 'electron-updater'

autoUpdater.on('update-downloaded', () => {
	autoUpdater.quitAndInstall()
})

app.on('ready', () => {
	if (process.env.NODE_ENV === 'production') autoUpdater.checkForUpdates()
})
 */
