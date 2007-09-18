package tDB2Output::DB2;

use Carp;

sub getTableCreationQuery {
    my %param = @_;

    my %talendtype_to_dbtype = (
        boolean    => 'SMALLINT',
        date     => 'DATE',
        datetime  => 'TIMESTAMP',
        decimal   => 'DECIMAL',
        int     => 'INTEGER',
        string  => 'VARCHAR',
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

        if (lc $column_href->{dbtype} eq 'varchar'
            or lc $column_href->{dbtype} eq 'decimal') {
            $query.= ' (';
            $query.= $column_href->{len};

            if (lc $column_href->{type} eq 'decimal') {
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

1;
