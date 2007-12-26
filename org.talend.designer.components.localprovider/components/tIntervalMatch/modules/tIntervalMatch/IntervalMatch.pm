package tIntervalMatch::IntervalMatch ;
use strict;
use Data::Dumper;


sub search {
    my ( %params ) = @_ ;
    my ( $bInf, $bSup ) = ( 0, scalar( @{$params{lookup_inf}} )-1 ) ;

    my $searched = $params{value} ;

    my $bCurrent = $bSup;

    my $result ;

    my $i = 0;
    while(1) {
        
        $bCurrent = $bInf + int (($bSup - $bInf) / 2);
        if ($searched > ${params{lookup_inf}}->[$bCurrent]) {
            $bInf = $bCurrent;
        }
        if ($searched < ${params{lookup_inf}}->[$bCurrent]) {
            $bSup = $bCurrent;
        }
        # found
        if ($searched == ${params{lookup_inf}}->[$bCurrent]){
            $result = $bCurrent ;
            last ;
        }

        # found
        if( $searched >= ${params{lookup_inf}}->[$bInf] and $searched < ${params{lookup_inf}}->[$bInf+1] ){
            $result = $bInf ;
            last ;
        }

        # found
        if( $searched >= ${params{lookup_inf}}->[$bSup] and $searched < ${params{lookup_sup}}->[$bSup] ){
            $result = $bSup ;
            last ;
        }

        last if $i > scalar( @{$params{lookup_inf}} );

        $i++;
    }
    return $result ;
}

1;