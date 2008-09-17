package tOracleOutput::Oracle;

use Carp;

sub getTableCreationQuery {
    my %param = @_;

    my %talendtype_to_dbtype = (
        boolean    => 'CHAR',
        date     => 'DATE',
        datetime  => 'DATE',
        decimal   => 'NUMBER',
        int     => 'NUMBER',
        string  => 'VARCHAR2',
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

    # Oracle creation table statement example
    #
    # create table sales
    # (
    #   shop_code number(9) not null,
    #   ean       varchar2(13) not null,
    #   sales     number(5,2),
    #   quantity  number(9),
    #   constraint sales_pk primary key (shop_code, ean)
    # )
    $query = 'CREATE TABLE '.$param{tablename}.' ('."\n";

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

        $query.= ''.$column_href->{name}.'';
        $query.= ' '.$column_href->{dbtype};

        if (lc $column_href->{dbtype} eq 'varchar2'
            or lc $column_href->{dbtype} eq 'number'
            or lc $column_href->{dbtype} eq 'char') {
            $query.= ' (';
            $query.= $column_href->{len};

            if ($column_href->{type} eq 'decimal') {
                $query.= ','.$column_href->{precision};
            }

            $query.= ')';
        }

        if (not $column_href->{null}) {
            $query.= ' NOT NULL';
        }

        if ($column_href->{default} != '') {
            $query.= " DEFAULT '".$column_href->{default}."'";
        }

        $query.= "\n";

        $column_num++;
    }

    if (@key_names) {
        $query.= sprintf(
            ", CONSTRAINT %s_pk PRIMARY KEY(%s)\n",
            $param{tablename},
            join(
                ',',
                @key_names
            )
        );
    }

    $query.= ')';

#     use Data::Dumper;
#     print Dumper($param{schema});
#     print $query; exit();

    return $query;
}

sub manage_encoding {
    my %param = @_;

    my $set_encoding = 1;
    if (defined $ENV{NLS_LANG} and !$param{override}) {
        $set_encoding = 0;
    }

    if ($set_encoding) {
        if ($param{encoding_type} eq 'UTF-8') {
            $ENV{NLS_LANG} = '.AL32UTF8';
        }

        if ($param{encoding_type} eq 'ISO-8859-15') {
            $ENV{NLS_LANG} = '.WE8ISO8859P15';
        }

        # if the encoding type is CUSTOM, we take the encoding "as is"
        if ($param{encoding_type} eq 'CUSTOM') {
            $ENV{NLS_LANG} = $param{encoding};
        }
    }
}

sub performTableAction {
    my %param = @_;

    # tableAction
    # dbschema
    # dbh
    # dbtable
    # component
    # schema

    my $sth;
    my $table_exists;

    if ($param{tableAction} eq "DROP_CREATE"
        or $param{tableAction} eq "CREATE_IF_NOT_EXISTS") {
        # We need the table list to know if drop or "create if not exists"
        # is relevant
        my $tabsth = $param{dbh}->prepare(
            '
select 1
  from all_tables
  where owner = ?
    and table_name = ?
'
        );

        $tabsth->execute(
            uc $param{dbschema},
            uc $param{dbtable}
        )
            or die "can't execute query : $!";

        while (my $row = $tabsth->fetchrow_arrayref()) {
            $table_exists = $row->[0];
            last;
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

    return $query;
}

1;
