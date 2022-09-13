/*
 * @Author: your name
 * @Date: 2021-05-10 15:54:54
 * @LastEditTime: 2021-08-26 17:29:26
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\api\modules\user.js
 */
import Ajax from '@/utils/request';

const user = {
	login: (params) => Ajax.post('/sso/login', params),

	loginOut: (params) => Ajax.post('/sso/loginOut', params),

	getInfo: (params) => Ajax.get('/sso/user/info', params),

	redirectLogin: (params) => Ajax.post('/sso/redirectLogin', params)
}

export default user;