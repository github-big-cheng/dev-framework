drop table if exists `ucenter`.`t_ucenter_log`;
create table `ucenter`.`t_ucenter_log`
(
    id           int(10) primary key auto_increment not null comment 'id',
    org_id       int(10) comment '机构id',
    org_name     varchar(128) comment '机构名称',
    login_time   varchar(20) comment '登录时间',
    type         varchar(32) comment '操作类型',
    ip           varchar(32) comment 'IP',
    machine_name varchar(128) comment '机器名称',
    user_id      int(10) comment '用户id',
    op_content   varchar(256) comment '操作内容',
    op_state     int(10) comment '操作状态',
    create_by    int(10) comment '创建人',
    create_time  varchar(20) comment '创建时间',
    update_by    int(10) comment '修改人',
    update_time  varchar(20) comment '修改时间',
    is_deleted   int(1) comment '是否删除 0-否 1-是'
) comment = '登录日志表';

drop table if exists `ucenter`.`t_ucenter_obj_func`;
create table `ucenter`.`t_ucenter_obj_func`
(
    id          int(10) primary key auto_increment not null comment 'id',
    org_id      int(10) comment '机构id',
    org_name    varchar(128) comment '机构名称',
    obj_type    varchar(32) comment '权限类型',
    obj_id      int(10) comment '类型id',
    func_id     int(10) comment '菜单id',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '对象权限表';

drop table if exists `ucenter`.`t_ucenter_org`;
create table `ucenter`.`t_ucenter_org`
(
    id                  int(10) primary key auto_increment not null comment 'id',
    org_id              int(10) comment '机构id',
    org_name            varchar(128) comment '机构名称',
    code                varchar(128) comment '代码',
    cname               varchar(128) comment '名称',
    comp_type           varchar(32) comment '机构类型',
    path_ids            varchar(1048) comment '路径ids',
    path_names          varchar(1048) comment '路径名称',
    parent_id           int(10) comment '父级id',
    ename               varchar(128) comment '英文名称',
    sname               varchar(128) comment '简称',
    link_id             int(10) comment '联系人id',
    linker              varchar(64) comment '联系人',
    linker_mobile_phone varchar(64) comment '联系人电话',
    telephone           varchar(64) comment '电话',
    fax                 varchar(64) comment '传真',
    order_no            int(10) comment '排序号',
    memo                varchar(512) comment '备注',
    op_state            int(10) comment '操作状态',
    create_by           int(10) comment '创建人',
    create_time         varchar(20) comment '创建时间',
    update_by           int(10) comment '修改人',
    update_time         varchar(20) comment '修改时间',
    is_deleted          int(1) comment '是否删除 0-否 1-是'
) comment = '机构表';


drop table if exists `ucenter`.`t_ucenter_org_info`;
create table `ucenter`.`t_ucenter_org_info`
(
    id          int(10) primary key auto_increment not null comment 'id',
    org_id      int(10) comment '机构表id',
    field_key   varchar(128) comment 'key',
    field_value varchar(256) comment '值',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '机构信息详情表';


drop table if exists `ucenter`.`t_ucenter_person`;
create table `ucenter`.`t_ucenter_person`
(
    id          int(10) primary key auto_increment not null comment 'id',
    org_id      int(10) comment '机构id',
    org_name    varchar(128) comment '机构名称',
    name        varchar(128) comment '姓名',
    id_no       varchar(32) comment '身份证号',
    job_no      varchar(32) comment '工号',
    sex         varchar(32) comment '性别',
    mobile      varchar(32) comment '手机号',
    telephone   varchar(128) comment '电话',
    email       varchar(256) comment 'email',
    marriage    varchar(32) comment '婚姻状况',
    birthday    varchar(20) comment '出生日期',
    nation      varchar(32) comment '国籍',
    status      varchar(32) comment '员工状态',
    order_no    int(10) comment '顺序号',
    memo        varchar(1024) comment '备注',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '人员表';

drop table if exists `ucenter`.`t_ucenter_person_info`;
create table `ucenter`.`t_ucenter_person_info`
(
    id          int(10) primary key auto_increment not null comment 'id',
    person_id   int(10) comment '人员id',
    field_key   varchar(128) comment 'key',
    field_value varchar(256) comment '值',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '人员信息详情表';


drop table if exists `ucenter`.`t_ucenter_person_org`;
create table `ucenter`.`t_ucenter_person_org`
(
    id             int(10) primary key auto_increment not null comment 'id',
    root_org_id    int(10) comment '机构id',
    dept_id        int(10) comment '部门id',
    person_id      int(10) comment '人员id',
    is_main        int(10) comment '是否主要部门',
    is_main_person int(10) comment '是否主要负责人',
    pos_id         int(10) comment '职位id',
    pos_level      varchar(32) comment '职级',
    order_no       int(10) comment '排序号',
    op_state       int(10) comment '操作状态',
    create_by      int(10) comment '创建人',
    create_time    varchar(20) comment '创建时间',
    update_by      int(10) comment '修改人',
    update_time    varchar(20) comment '修改时间',
    is_deleted     int(1) comment '是否删除 0-否 1-是'
) comment = '部门人员表';


drop table if exists `ucenter`.`t_ucenter_person_role`;
create table `ucenter`.`t_ucenter_person_role`
(
    id          int(10) primary key auto_increment not null comment 'id',
    person_id   int(10) comment '人员id',
    role_id     int(10) comment '角色id',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '人员角色表';


drop table if exists `ucenter`.`t_ucenter_position`;
create table `ucenter`.`t_ucenter_position`
(
    id          int(10) primary key auto_increment not null comment 'id',
    org_id      int(10) comment '机构id',
    org_name    varchar(128) comment '机构名称',
    code        varchar(32) comment '代码',
    name        varchar(32) comment '名称',
    type        varchar(128) comment '类型',
    memo        varchar(512) comment '备注',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
) comment = '职位表';

drop table if exists `ucenter`.`t_ucenter_role`;
create table `ucenter`.`t_ucenter_role`
(
    id          int(10) primary key auto_increment not null comment 'id',
    org_id      int(10) comment '机构id',
    org_name    varchar(128) comment '机构名称',
    name        varchar(64) comment '角色名称',
    code        varchar(64) comment '角色代码',
    order_no    int(10) comment '排序号',
    memo        varchar(512) comment '备注',
    is_sys      int(10) comment '是否系统默认',
    op_state    int(10) comment '操作状态',
    create_by   int(10) comment '创建人',
    create_time varchar(20) comment '创建时间',
    update_by   int(10) comment '修改人',
    update_time varchar(20) comment '修改时间',
    is_deleted  int(1) comment '是否删除 0-否 1-是'
);

drop table if exists `ucenter`.`t_ucenter_user`;
create table `ucenter`.`t_ucenter_user`
(
    id            int(10) primary key auto_increment not null comment 'id',
    org_id        int(10) comment '机构id',
    org_name      varchar(128) comment '机构名称',
    person_id     int(10) comment '人员id',
    account       varchar(32) comment '账号',
    password      varchar(256) comment '密码',
    salt          varchar(256) comment '盐',
    first_login   int(10) comment '是否首次登录',
    is_error_time int(10) comment '错误次数',
    status        int(10) comment '状态',
    op_state      int(10) comment '操作状态',
    create_by     int(10) comment '创建人',
    create_time   varchar(20) comment '创建时间',
    update_by     int(10) comment '修改人',
    update_time   varchar(20) comment '修改时间',
    is_deleted    int(1) comment '是否删除 0-否 1-是'
) comment = '用户信息表';