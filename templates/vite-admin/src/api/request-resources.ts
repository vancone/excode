import { AxiosRequestConfig } from "axios";
import { RequestSource } from "./types";

const requestSources: RequestSource[] = []
export function removeResource(config: AxiosRequestConfig) {
    const { url, method } = config
    const index = requestSources.findIndex(
        ({
            method: resourceMethod,
            url: resourceUrl
        }) => (resourceMethod === method && resourceUrl === url)
    )

    if (index >= 0) {
        requestSources[index].cancel('Request canceled')
        requestSources.splice(index, 1)
    }
}

export function storeResource(resource: RequestSource) {
    requestSources.push(resource)
}

export function clearResources() {
    if (!requestSources.length) {
        return
    }
    requestSources.forEach(({ cancel }) => {
        cancel('Request canceled')
    })

    requestSources.splice(0, requestSources.length)
}