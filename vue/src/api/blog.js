import request from '@/utils/request';

/**
 * 添加、修改博客
 * @param data 博客
 * @returns {*} 结果
 */
export const saveBlog = (data) => {
    return request({
        url: '/blog', method: 'POST', data: data
    })
}

/**
 * 删除博客
 * @param ids ID列表
 * @returns {*} 结果
 */
export const removeBlogBatchByIds = (ids) => {
    return request({
        url: `/blog/${ids}`, method: 'DELETE'
    })
}

/**
 * 查询博客列表
 * @param params 博客
 * @returns {*} 结果
 */
export const getBlogList = (params) => {
    return request({
        url: '/blog/list', method: 'GET', params: params
    })
}

/**
 * 查询博客分页
 * @param params 博客
 * @returns {*} 结果
 */
export const getBlogPage = (params) => {
    return request({
        url: '/blog/page', method: 'GET', params: params
    })
}

/**
 * 查询博客
 * @param params 博客
 * @returns {*} 结果
 */
export const getBlogOne = (params) => {
    return request({
        url: '/blog', method: 'GET', params: params
    })
}
