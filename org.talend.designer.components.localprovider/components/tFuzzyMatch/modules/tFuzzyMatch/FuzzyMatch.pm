package tFuzzyMatch::FuzzyMatch ;
use strict;
use Text::LevenshteinXS qw(distance);
use Text::Metaphone qw(Metaphone);

# unique : stop after the first successful matching result
# lookup : values to lookup
# dmin : min value
# dmax : max value
# value : value to be matched
sub matchLevenshteinDistance {
    my ( %params ) = @_ ;
    
    die "Min cannot be greater than Max ( $params{dmin}, $params{dmax} )\n" if $params{dmin} > $params{dmax} ;
    my $lk_candidates = {} ;
    my $best = $params{dmax} ;
    foreach my $lookup ( @{$params{lookup}} ){
	# specific to editdistance !!!
        next if abs( ( length($params{value}) - length($lookup) ) ) > $params{dmax} ;
        last if ( $params{unique} && ( $best eq $params{dmin}) );
        my $dist = $params{sensitive} ? distance( $params{value}, $lookup ) : distance( lc $params{value}, lc $lookup ) ;
        next if $dist < $params{dmin} ;
        next if $dist > $params{dmax} ; 
        next if $dist > $best ;
        $lk_candidates->{$dist} ||= [] ;
        push @{$lk_candidates->{$dist}}, $lookup ;
        $best = $dist ;
    }
    
    my $result = {} ;
    $result->{$best} = $lk_candidates->{$best} ;
    # resultset is empty
    delete $result->{$best} unless( $result->{$best} ) ;
    return $result ;
}

# unique : stop after the first successful matching result
# lookup : values to lookup
# value : value to be matched
sub matchMetaphone {
    my ( %params ) = @_ ;

    my $result = {} ;
    my $phoned_value = Metaphone($params{value});
    $result->{$phoned_value} = [] ;
    foreach my $lookup ( @{$params{lookup}} ){
        last if ( $params{unique} && scalar(@{$result->{$phoned_value}}) );
        my $phoned_lookup = Metaphone( $lookup ) ;
        next unless $phoned_value eq $phoned_lookup ;
        push @{$result->{$phoned_value}}, $lookup ;
    }
    
    # resultset is empty
    delete $result->{$phoned_value} unless( $result->{$phoned_value} ) ;
    return $result ;
	
}

1;