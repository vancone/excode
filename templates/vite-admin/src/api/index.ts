import service from './request'
import {
    IAnyObject,
    ICumulus,
    Response
} from './types'

export const getCumulusData = (params?: IAnyObject) => service.get<Response<ICumulus>>(
    '/api/cumulus/data',
    { params }
)