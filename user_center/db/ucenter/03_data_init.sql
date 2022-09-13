use `ucenter`;


-- 初始化角色
insert into `ucenter`.`t_ucenter_role` (id, org_id, org_name, name, code, order_no, is_sys, op_state, create_by, create_time, update_by, update_time, is_deleted, memo) values (1, -1, '用户中心', '超级管理员', 'admin', 1, 1, null, 1, now(), null, null, 0, '超级管理员，所有菜单及数据权限');


-- 初始化人员
insert into `ucenter`.`t_ucenter_person` (id, org_id, org_name, name, sex, telephone, status, create_by, create_time, is_deleted) values (1, -1, '用户中心', '超级管理员', '10004-10', '18902101234', 1, null, now(), 0);
insert into `ucenter`.`t_ucenter_user` (id, org_id, org_name, person_id, account, password, salt, first_login, status, create_by, create_time, is_deleted) values (1, -1, '用户中心', 1, 'admin', '600b3cfd2adb6356cddf98b1d95e6c35', '5E5D705', 1, 1, 28, now(), 0);
insert into `ucenter`.`t_ucenter_person_role` (person_id, role_id, create_by, create_time, is_deleted) values (1, 1, 1, now(), 0);

commit;