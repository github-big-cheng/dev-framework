drop table if exists `sys`.`t_sys_code`;
create table `sys`.`t_sys_code`
(
    id          int(10) primary key auto_increment not null comment 'id',
    value       varchar(64) comment 'id',
    type        varchar(32) comment '类型',
    locale      varchar(64) comment '本地化',
    path_value  varchar(64) comment '路径',
    code        varchar(64) comment '代码值',
    name        varchar(512) comment '名称',
    order_no    int(10) comment '排序号',
    parent      varchar(32) comment '父级',
    is_sys      int(10) comment '代码类型 1-系统 0-用户',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '系统代码表';

-- 添加唯一索引
ALTER TABLE `sys`.`t_sys_code`
    ADD UNIQUE (`value`, `locale`);


drop table if exists `sys`.`t_sys_project`;
create table `sys`.`t_sys_project`
(
    id          int(10) primary key auto_increment not null comment 'id',
    name        varchar(64) comment '模块名称',
    code        varchar(64) comment '模块代码',
    order_no    int(10) comment '排序号',
    type        int(10) comment '模块类型',
    path        varchar(128) comment '模块路径',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '模块表';


drop table if exists `sys`.`t_sys_function`;
create table `sys`.`t_sys_function`
(
    id          int(10) primary key not null comment 'id',
    name        varchar(64) comment '菜单名称',
    action      varchar(512) comment '资源路径',
    parent_id   int(10) comment '父级id',
    code        varchar(64) comment '代码',
    type        int(10) comment '菜单类型 1-菜单 2-子菜单 3-tab页签 4-按钮',
    description varchar(64) comment '功能简介',
    img_path    varchar(128) comment '图片地址',
    order_no    int(10) comment '排序号',
    project_id  int(10) comment '所属项目id',
    is_phone    int(10) comment '是否手机端菜单',
    phone_type  int(10) comment '手机类型',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '功能菜单表';

drop table if exists `sys`.`t_sys_parameter`;
create table `sys`.`t_sys_parameter`
(
    id          int(10) primary key auto_increment not null comment 'id',
    org_id      int(10) comment '机构id',
    org_name    varchar(128) comment '机构名称',
    keies       varchar(64) comment 'key',
    `value`     varchar(512) comment 'value',
    description varchar(512) comment '描述',
    type        varchar(128) comment '参数类型',
    sub_type    varchar(258) comment '参数子类型',
    remark      varchar(512) comment '备注',
    order_no    int(10) comment '排序',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '系统参数表';

drop table if exists `sys`.t_sys_column_config;
create table `sys`.t_sys_column_config
(
    id          int(10) primary key auto_increment not null comment 'id',
    user_id     int(10) comment '用户id',
    `code`      varchar(64) comment '代码',
    `name`      varchar(128) comment '名称',
    col_key     varchar(64) comment '字段key',
    col_name    varchar(64) comment '字段名称',
    col_width   decimal(10, 4) comment '列宽',
    height      decimal(10, 4) comment '行高',
    is_selected int(1) comment '是否被选中 1-是 0-否',
    order_no    int(5) comment '排序号',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '自定义列表配置表';

drop table if exists `sys`.t_sys_file;
create table `sys`.`t_sys_file`
(
    id          int(10) primary key auto_increment not null comment 'id',
    org_id      int(10) comment '机构id',
    org_name    varchar(128) comment '机构名称',
    upload_time varchar(20) comment '上传时间',
    attach_size bigint(20) comment '附件大小（字节）',
    source_type varchar(32) comment '业务类型',
    sub_type    varchar(32) comment '业务子类型',
    source_id   int(10) comment '业务id',
    filename    varchar(128) comment '文件名称',
    path        varchar(256) comment '保存路径',
    real_path   varchar(128) comment '实际名称',
    extension   varchar(64) comment '扩展名 jpg|gif|xls|doc|...',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '附件表';

drop table if exists `sys`.t_sys_operate_log;
create table `sys`.`t_sys_operate_log`
(
    id           int(10) primary key auto_increment not null comment 'id',
    `name`       varchar(64) comment '操作人',
    ip           varchar(32) comment 'IP地址',
    machine_name varchar(128) comment '机器名称',
    user_id      int(10) comment '用户id',
    op_time      varchar(20) comment '操作时间',
    request_msg  text comment '请求内容',
    response_msg text comment '相应内容',
    op_state     int(10) comment '操作状态',
    create_by    int(10) comment '创建人',
    create_time  varchar(20) comment '创建时间',
    update_by    int(10) comment '修改人',
    update_time  varchar(20) comment '修改时间',
    is_deleted   int(1) comment '是否删除 0-否 1-是'
) comment = '操作日志表';