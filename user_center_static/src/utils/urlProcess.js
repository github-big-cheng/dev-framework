import Ajax from '@/utils/request';
/**
 * url处理
 * @param {
 *          name:'请求名称',
 *          url:'公共地址',
 *          types: '请求具体地址',Array[ String | [ url, 请求方式 ]  ]
 *        }  
 * @returns { key :(params, urlType)=> Ajax.post } 
 * @key name + Types
 */

export function titleCase(str) {
    return str.replace(/( |^)[a-z]/g, (L) => L.toUpperCase());
}
export function urlProcess(...arg) {
    const obj = {}

    arg.forEach(({ name, url, types }) => {
        types.forEach(i => {
            let requestType = 'post'
            let type = i
            if (Array.isArray(i)) {
                requestType = i[1];
                type = i[0];
            }
            const key = name + titleCase(type)
            obj[key] = (params, urlType) => Ajax[requestType](`${url}/${type}${urlType ? ('/' + urlType) : ''}`, params)
        });
    })

    return obj
}
