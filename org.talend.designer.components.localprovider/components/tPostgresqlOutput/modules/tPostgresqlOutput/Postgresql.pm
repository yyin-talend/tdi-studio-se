package tPostgresqlOutput::Postgresql;

use Carp;

sub getTableCreationQuery {
    my %param = @_;

    my %talendtype_to_dbtype = (
        boolean  => 'BOOLEAN',
        date     => 'DATE',
        datetime => 'TIMESTAMP',
        int      => 'INTEGER',
        decimal  => 'NUMERIC',
        string   => 'VARCHAR',
    );

    # In $param{schema}, each column looks like this:
    #
    # {
    #     name    => 'shop_code',
    #     key     => true,
    #     type    => 'int',
    #     len     => null,
    #     precision => null,
    #     null    => false,
    #     default => '',
    #     comment => '',
    # }

    my $query;
    my $column_num = 1;
    my @key_names = map { $_->{name} } grep { $_->{key} } @{ $param{schema} };


    # PostgreSQL creation table statement example
    #
    # CREATE TABLE sales_copy (
    #   shop_code integer NOT NULL,
    #   ean char(13) NOT NULL,
    #   sales integer default NULL,
    #   quantity integer default NULL
    #   primary key(shop_code, ean)
    # );
    $query = 'CREATE TABLE '.$param{dbschema}.'.'.$param{tablename}.' ('."\n";

    foreach my $column_href (@{ $param{schema} }) {
        $column_href->{dbtype} = $talendtype_to_dbtype{$column_href->{type}};

        if (lc $column_href->{type} eq 'string') {
            if (not defined $column_href->{len}
                or $column_href->{len} == -1) {
                $column_href->{len} = 255;
            }
        }

        if ($column_num++ > 1) {
            $query.= ', ';
        }

        $query.= $column_href->{name};
        $query.= ' '.$column_href->{dbtype};

        if (grep /^$column_href->{dbtype}$/, qw/NUMERIC VARCHAR/
            and defined $column_href->{len}
            and $column_href->{len} != -1
        ) {
            $query.= '(';
            $query.= $column_href->{len};

            if (grep /^$column_href->{dbtype}$/, qw/NUMERIC/
                and defined $column_href->{precision}
                and $column_href->{precision} != -1
            ) {
                $query.= ','.$column_href->{precision};
            }

            $query.= ')';
        }

        if (not $column_href->{null}) {
            $query.= ' NOT NULL';
        }

        if ($column_href->{default} ne '') {
            $query.= " default '".$column_href->{default}."'";
        }

        $query.= "\n";

        $column_num++;
    }

    if (@key_names) {
        $query.= sprintf(
            ", PRIMARY KEY(%s)\n",
            join(
                ',',
                @key_names
            )
        );
    }

    $query.= ')';

#     use Data::Dumper;
#     print Dumper($param{schema});
#    print $query; exit();

    return $query;
}

sub performTableAction {
    my %param = @_;


    # tableAction
    # dbschema
    # dbh
    # dbtable
    # dbschema
    # component
    # schema

    my $sth;
    my $table_exists;

    if ($param{tableAction} eq "DROP_CREATE"
        or $param{tableAction} eq "CREATE_IF_NOT_EXISTS") {
        # We need the table list to know if drop or "create if not exists"
        # is relevant
        my $schema = $param{dbschema};
        my $catalog = undef;
        my $tabsth = $param{dbh}->table_info($catalog, $schema);
        my @tables = ();

        while (my $entity = $tabsth->fetchrow_hashref()) {
            if ($entity->{TABLE_TYPE} eq 'TABLE') {
                push @tables, lc $entity->{TABLE_NAME};
            }
        }

        # print "===\n";
        # print "existing tables:\n";
        # print join("\n", map {"  - ".$_} @tables), "\n";
        # print "===\n";

        my $test_table = lc $param{dbtable};
        $table_exists = grep /^$test_table$/, @tables;
    }

    if ($param{tableAction} eq "DROP_CREATE" and $table_exists) {
        $query = sprintf(
            'DROP TABLE %s.%s',
            $param{dbschema},
            $param{dbtable},
        );

        $sth = $param{dbh}->prepare($query);

        $sth->execute()
            or die sprintf("[%s] can't drop table", $param{component});

        # the table does not exist anymore
        $table_exists = 0;
    }

    if ($param{tableAction} eq "CREATE"
        or $param{tableAction} eq "DROP_CREATE"
        or ($param{tableAction} eq "CREATE_IF_NOT_EXISTS"
            and not $table_exists)) {
        # now we create the table
        $query = getTableCreationQuery(
            tablename  => $param{dbtable},
            dbschema   => $param{dbschema},
            schema     => $param{schema},
        );

        $sth = $param{dbh}->prepare($query);
        $sth->execute()
            or die sprintf(
                "[%s] cannot create table\n===\n%s\n===\n",
                $param{component},
                $query,
            );
    }

    if ($param{tableAction} eq "CLEAR") {
        $query = sprintf(
            'DELETE FROM %s.%s',
            $param{dbschema},
            $param{dbtable},
        );

        $sth = $param{dbh}->prepare($query);
        $sth->execute()
            or die sprintf(
                "[%s] cannot clear table",
                $param{component}
            );
    }

    if ($param{tableAction} eq "TRUNCATE") {
        $query = sprintf(
            'TRUNCATE %s.%s',
            $param{dbschema},
            $param{dbtable},
        );

        $sth = $param{dbh}->prepare($query);
        $sth->execute()
            or die sprintf(
                "[%s] cannot truncate table",
                $param{component}
            );
    }
}

1;
