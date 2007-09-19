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

        if (grep /^$column_href->{dbtype}$/, qw/INTEGER NUMERIC VARCHAR/
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

1;
