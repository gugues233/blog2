import request from '@/utils/request';

/**
 * 添加、修改博客标签关系
 * @param data 博客标签关系
 * @returns {*} 结果
 */
export const saveBlogTag = (data) => {
    return request({
        url: '/blogTag', method: 'POST', data: data
    })
}

/**
 * 删除博客标签关系
 * @param ids ID列表
 * @returns {*} 结果
 */
export const removeBlogTagBatchByIds = (ids) => {
    return request({
        url: `/blogTag/${ids}`, method: 'DELETE'
    })
}

/**
 * 查询博客标签关系列表
 * @param params 博客标签关系
 * @returns {*} 结果
 */
export const getBlogTagList = (params) => {
    return request({
        url: '/blogTag/list', method: 'GET', params: params
    })
}

/**
 * 查询博客标签关系分页
 * @param params 博客标签关系
 * @returns {*} 结果
 */
export const getBlogTagPage = (params) => {
    return request({
        url: '/blogTag/page', method: 'GET', params: params
    })
}

/**
 * 查询博客标签关系
 * @param params 博客标签关系
 * @returns {*} 结果
 */
export const getBlogTagOne = (params) => {
    return request({
        url: '/blogTag', method: 'GET', params: params
    })
}
