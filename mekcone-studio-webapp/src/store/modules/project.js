import storage from 'store'

const project = {
  state: {
    id: '',
    name: '',
    modules: [],
    modifiedTime: ''
  },
  mutations: {
    id (state, id) {
      state.id = id
      storage.set('id', id)
    },
    name (state, name) {
      state.name = name
      storage.set('name', name)
    },
    modules (state, modules) {
      state.modules = modules
      storage.set('modules', modules)
    }
  }
}

export default project
