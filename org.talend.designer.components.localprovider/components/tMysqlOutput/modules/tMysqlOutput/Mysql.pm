package tMysqlOutput::Mysql;

use Carp;

sub getTableCreationQuery {
    my %param = @_;

    my %talendtype_to_dbtype = (
        boolean    => 'BOOL',
        date     => 'DATE',
        datetime  => 'DATETIME',
        decimal   => 'DECIMAL',
        int     => 'INT',
        string  => 'VARCHAR',
    );

    my @dont_need_length_types = qw/DATE DATETIME/;

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

    # MySQL creation table statement example
    #
    # CREATE TABLE `sales_copy` (
    #   `shop_code` int(11) NOT NULL,
    #   `ean` char(13) NOT NULL,
    #   `sales` int(11) default NULL,
    #   `quantity` int(11) default NULL,
    #   primary key(shop_code, ean)
    # );
    $query = 'CREATE TABLE '.$param{tablename}.' ('."\n";

    foreach my $column_href (@{ $param{schema} }) {
        $column_href->{dbtype} = $talendtype_to_dbtype{$column_href->{type}};

        if ($column_num++ > 1) {
            $query.= ', ';
        }

        $query.= $column_href->{name};
        $query.= ' '.$column_href->{dbtype};

        if (not grep /^$column_href->{dbtype}$/, @dont_need_length_types) {
            if ($column_href->{dbtype} eq 'VARCHAR') {
                if (not defined $column_href->{len}
                    or $column_href->{len} <= 0) {
                    $column_href->{len} = 255;
                }
            }

            if (defined $column_href->{len}
                and $column_href->{len} > 0) {

                $query.= '(';
                $query.= $column_href->{len};

                if (grep /^$column_href->{dbtype}$/, qw/DECIMAL/) {
                    # DECIMAL
                    $query.= ','.$column_href->{precision};
                }

                $query.= ')';
            }
        }

        if (not $column_href->{null}) {
            $query.= ' NOT NULL';
        }

        if ($column_href->{default} != '') {
            $query.= " DEFAULT '".$column_href->{default}."'";
        }

        if ($column_href->{comment} != '') {
            $query.= sprintf(" COMMENT '%s'", $column_href->{comment});
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

#    use Data::Dumper;
#    print Dumper($param{schema});
#    print $query; exit();

    return $query;
}

sub getInsertQuery {
    my ($table, $keyString, $parametersString, $nbInserts) = @_;

    $query = '
INSERT
  INTO '.$table.'
  ('.$keyString.')
  VALUES
';

    return $query.join(
        "\n  ,",
        map {'('.$parametersString.')'} (1..$nbInserts)
    );
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
        my $tabsth = $param{dbh}->prepare('SHOW TABLES');
        $tabsth->execute();
        $table_exists = 0;
        my $test_table = lc $param{dbtable};

        while (my $row_aref = $tabsth->fetchrow_arrayref()) {
            if (lc $row_aref->[0] eq $test_table) {
                $table_exists = 1;
                last;
            }
        }
        $tabsth->finish();
    }

    if ($param{tableAction} eq "DROP_CREATE" and $table_exists) {
        $param{dbh}->do('DROP TABLE '.$param{dbtable})
            or die sprintf("[%s] can't drop table", $param{component});

        $table_exists = 0;
    }

    if ($param{tableAction} eq "CREATE"
        or $param{tableAction} eq "DROP_CREATE"
        or ($param{tableAction} eq "CREATE_IF_NOT_EXISTS"
            and not $table_exists)) {

        # now we create the table
        $query = getTableCreationQuery(
            tablename  => $param{dbtable},
            schema     => $param{schema},
        );

        $param{dbh}->do($query)
            or die sprintf(
                "[%s] cannot create table\n===\n%s\n===\n",
                $param{component},
                $query,
            );
    }

    if ($param{tableAction} eq "CLEAR") {
        $param{dbh}->do('DELETE FROM '.$param{dbtable})
            or die sprintf(
                "[%s] can't clear table",
                $param{component}
            );
    }

    if ($param{tableAction} eq "TRUNCATE") {
        $param{dbh}->do('TRUNCATE TABLE '.$param{dbtable})
            or die sprintf(
                "[%s] cannot truncate table",
                $param{component}
            );
    }

}

1;
