import request from '@/utils/request';

/**
 * 添加、修改标签
 * @param data 标签
 * @returns {*} 结果
 */
export const saveTag = (data) => {
    return request({
        url: '/tag', method: 'POST', data: data
    })
}

/**
 * 删除标签
 * @param ids ID列表
 * @returns {*} 结果
 */
export const removeTagBatchByIds = (ids) => {
    return request({
        url: `/tag/${ids}`, method: 'DELETE'
    })
}

/**
 * 查询标签列表
 * @param params 标签
 * @returns {*} 结果
 */
export const getTagList = (params) => {
    return request({
        url: '/tag/list', method: 'GET', params: params
    })
}

/**
 * 查询标签分页
 * @param params 标签
 * @returns {*} 结果
 */
export const getTagPage = (params) => {
    return request({
        url: '/tag/page', method: 'GET', params: params
    })
}

/**
 * 查询标签
 * @param params 标签
 * @returns {*} 结果
 */
export const getTagOne = (params) => {
    return request({
        url: '/tag', method: 'GET', params: params
    })
}
