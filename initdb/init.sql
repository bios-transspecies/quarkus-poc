CREATE SCHEMA docker AUTHORIZATION korsarz;
create table docker.article_comment
(
    article_id  int8 not null,
    comments_id int8 not null,
    primary key (article_id, comments_id)
);
create table docker.article_tag
(
    article_id int8         not null,
    tags_value varchar(255) not null,
    primary key (article_id, tags_value)
);
create table docker.comment
(
    id        int8 not null,
    content   text,
    date      timestamp,
    author_id int8,
    primary key (id)
);
create table docker.comment_comment
(
    comment_id  int8 not null,
    comments_id int8 not null
);
create table docker.company
(
    id                int8 not null,
    registered_number varchar(255),
    vat_number        varchar(255),
    country_id        int8 not null,
    primary key (id)
);
create table docker.controller
(
    id          int8    not null,
    active      boolean not null,
    controller  varchar(255),
    http_method varchar(255),
    method      varchar(255),
    primary key (id)
);
create table docker.controller_permission
(
    controllers_id int8 not null,
    permissions_id int8 not null,
    primary key (controllers_id, permissions_id)
);
create table docker.country
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);
create table docker.document
(
    id            int8 not null,
    date          timestamp,
    document_name varchar(255),
    key           varchar(255),
    row           int4,
    value         TEXT,
    user_id       int8,
    primary key (id)
);
create table docker.files_profiles
(
    user_profile_id int8 not null,
    files_id        int8 not null,
    primary key (user_profile_id, files_id)
);
create table docker.followers
(
    user_profile_id int8 not null,
    followers_id    int8 not null,
    primary key (user_profile_id, followers_id)
);
create table docker.following
(
    user_profile_id int8 not null,
    following_id    int8 not null,
    primary key (user_profile_id, following_id)
);
create table docker.friends
(
    user_profile_id int8 not null,
    friends_id      int8 not null,
    primary key (user_profile_id, friends_id)
);
create table docker.mail_box
(
    id        int8    not null,
    host      varchar(255),
    password  varchar(255),
    port      int4    not null,
    protocol  varchar(255),
    smtp_auth boolean not null,
    tls       boolean not null,
    username  varchar(255),
    owner_id  int8,
    primary key (id)
);
create table docker.permission
(
    id             int8    not null,
    active         boolean not null,
    from_date      timestamp,
    to_date        timestamp,
    created_by     int4,
    permission_for int4,
    primary key (id)
);
create table docker.role
(
    id         int8    not null,
    active     boolean not null,
    created    timestamp,
    created_by varchar(255),
    edited     timestamp,
    edited_by  varchar(255),
    name       varchar(255),
    primary key (id)
);
create table docker.role_controller
(
    role_id       int8 not null,
    controller_id int8 not null,
    primary key (role_id, controller_id)
);
create table docker.scraped_email
(
    id    int8 not null,
    email varchar(255),
    primary key (id)
);
create table docker.scraped_resource
(
    id          int8 not null,
    access_date timestamp,
    content     varchar(100000),
    url         varchar(500),
    primary key (id)
);
create table docker.tag
(
    value varchar(255) not null,
    primary key (value)
);
create table docker.tag_article
(
    tag_value   varchar(255) not null,
    articles_id int8         not null,
    primary key (tag_value, articles_id)
);
create table docker.ticket
(
    id          int8         not null,
    created     TIMESTAMP,
    deleted     boolean,
    description varchar(30),
    disabled    TIMESTAMP,
    state       int4,
    title       varchar(255) not null,
    assigned_to int4,
    created_by  int4         not null,
    primary key (id)
);
create table docker.ticket_user
(
    ticket_id int8 not null,
    user_id   int8 not null
);
create table docker.ticket_activity
(
    id                  int8      not null,
    comment             varchar(10000),
    date                timestamp not null,
    ticket_state_after  varchar(20),
    ticket_state_before varchar(20),
    ticket_id           int8      not null,
    user_id             int8      not null,
    primary key (id)
);
create table docker.uploaded
(
    id           int8 not null,
    cathegory    varchar(255),
    description  varchar(255),
    file_name    varchar(255),
    file_storage varchar(255),
    hide         boolean,
    logged_only  boolean,
    priv         boolean,
    title        varchar(255),
    owner_id     int8,
    primary key (id)
);
create table docker.uploaded_user_profile
(
    uploaded_id         int8 not null,
    profile_assigned_id int8 not null,
    primary key (uploaded_id, profile_assigned_id)
);
create table docker.user
(
    id                 int8        not null,
    activated          timestamp,
    active             boolean     not null,
    created            timestamp,
    last_logged        timestamp,
    login              varchar(30) not null,
    name               varchar(30),
    password_changed   timestamp,
    password_encrypted varchar(255),
    surname            varchar(30),
    primary key (id)
);
create table docker.user_role
(
    users_id int8 not null,
    roles_id int8 not null,
    primary key (users_id, roles_id)
);
create table docker.user_profile
(
    id                  int8 not null,
    brand               varchar(255),
    description         varchar(255),
    enabled             boolean,
    link                varchar(255),
    personal            boolean,
    background_photo_id int8,
    company_id          int8,
    profie_photo_id     int8,
    user_id             int8 not null,
    primary key (id)
);
create table docker.warning
(
    id        int8         not null,
    confirmed timestamp,
    created   timestamp,
    hide      boolean,
    message   varchar(255) not null,
    presented timestamp,
    warned_id int8,
    primary key (id)
);
create table docker.websites_emails
(
    scraped_resource_id int8 not null,
    emails_id           int8 not null,
    primary key (scraped_resource_id, emails_id)
);
create table docker.websites_relations
(
    scraped_resource_id int8 not null,
    connections_id      int8 not null,
    primary key (scraped_resource_id, connections_id)
);
create table docker.worker
(
    id           int8 not null,
    company_role varchar(255),
    company_id   int8,
    user_id      int8,
    primary key (id)
);
