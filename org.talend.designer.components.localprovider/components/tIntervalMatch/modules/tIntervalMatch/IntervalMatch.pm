package tIntervalMatch::IntervalMatch ;
use strict;


sub search {
    my ( %params ) = @_ ;
    my ( $bInf, $bSup ) = ( 0, scalar( @{$params{lookup}} ) ) ;

    my $searched = $params{value} ;

    my $bCurrent = $bSup;

    my $result ;

    while(1) {
        $bCurrent = $bInf + int (($bSup - $bInf) / 2);
        if ($searched > ${params{lookup}}->[$bCurrent]) {
            $bInf = $bCurrent;
        }
        if ($searched < ${params{lookup}}->[$bCurrent]) {
            $bSup = $bCurrent;
        }
        # found
        if ($searched == ${params{lookup}}->[$bCurrent]){
            $result = $bCurrent ;
            last ;
        }

        # found
        if( $searched >= ${params{lookup}}->[$bInf] and $searched < ${params{lookup}}->[$bInf+1] ){
            $result = $bInf ;
            last ;
        }
        # not found
        last if (($searched > ${params{lookup}}->[$bSup]) or ($searched < ${params{lookup}}->[$bInf])) ;
    }
    return $result ;
}

1;