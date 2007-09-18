package tSybaseOutput::Sybase;

use Carp;

sub getTableCreationQuery {
    my %param = @_;

    my %talendtype_to_dbtype = (
        boolean    => 'TINYINT',
        date     => 'SMALLDATETIME',
        datetime  => 'DATETIME',
        decimal   => 'DECIMAL',
        int     => 'INT',
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
#     print $query;
#     exit();

    return $query;
}

1;
