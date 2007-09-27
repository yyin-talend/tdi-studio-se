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

1;
