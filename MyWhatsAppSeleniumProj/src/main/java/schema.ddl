
    create table Bulletin (
       id bigint not null,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table DistributionList (
       id bigint not null,
        name varchar(255) not null,
        owner_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table DistributionList_Member (
       DistributionList_id bigint not null,
        destinations_id bigint not null
    ) engine=InnoDB;

    create table hibernate_sequence (
       next_val bigint
    ) engine=InnoDB;

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    create table Member (
       id bigint not null,
        mobilenb varchar(255) not null,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table User (
       id bigint not null,
        created datetime,
        secret varchar(255) not null,
        updated datetime,
        userid varchar(25) not null,
        primary key (id)
    ) engine=InnoDB;

    alter table DistributionList 
       add constraint FKdgul01bsempdxfgtqioymbx0q 
       foreign key (owner_id) 
       references User (id);

    alter table DistributionList_Member 
       add constraint FK2jascikf3nrff1swyd646p8bi 
       foreign key (destinations_id) 
       references Member (id);

    alter table DistributionList_Member 
       add constraint FKbl5fw567u8dn4ia77t1ff95bn 
       foreign key (DistributionList_id) 
       references DistributionList (id);
