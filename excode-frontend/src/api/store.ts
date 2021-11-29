import { reactive, ref } from "vue"
import { queryProject, updateProject } from "."
import { IProject } from "./types"

const editted = ref(false)

const project: IProject = reactive({
    id: '',
    name: '',
    version: '',
    description: '',
    createdTime: '',
    updatedTime: ''
})

function setProject(val: IProject) {
    Object.assign(project, val)
    editted.value = true
    saveProject()
}

function getProject(): IProject {
    return project
}

function loadProject() {
    const projectId = sessionStorage.getItem('projectId')
    if (projectId === null) {
        alert('ERROR')
        return
    }
    queryProject(projectId).then(({ data }) => {
        if (data.code === 0) {
            editted.value = false
        }
    })
}

function saveProject() {
    updateProject(project).then(({ data }) => {
        if (data.code === 0) {
            editted.value = false
        }
    })
}

export {
    editted,
    getProject,
    loadProject,
    saveProject,
    setProject
}