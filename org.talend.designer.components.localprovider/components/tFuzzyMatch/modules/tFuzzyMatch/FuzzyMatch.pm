package tFuzzyMatch::FuzzyMatch ;
use strict;
use Text::LevenshteinXS qw(distance);
use Text::Metaphone qw(Metaphone);
use Text::DoubleMetaphone qw(double_metaphone);
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
        next if abs( ( length($params{value}) - length($lookup) ) ) > $params{dmax} ;
        my $dist = $params{sensitive} ? distance( $params{value}, $lookup ) : distance( lc $params{value}, lc $lookup ) ;
        next if $dist < $params{dmin} ;
        next if $dist > $params{dmax} ; 
        next if $dist > $best ;
        $lk_candidates->{$dist} ||= [] ;
        push @{$lk_candidates->{$dist}}, $lookup ;
        $best = $dist ;
        last if ( $params{unique} && ( $best eq $params{dmin}) );
    }
    
    my $result = {} ;
    $result->{$best} = $lk_candidates->{$best} ;
    # resultset is empty
    delete $result->{$best} unless( $result->{$best} ) ;
    return $result ;
}

# lookup : values to lookup
# value : value to be matched
sub matchMetaphone {
    my ( %params ) = @_ ;

    my $result = {} ;
    my $phoned_value = Metaphone($params{value});
    my $lookup_result = $params{lookup}->{$phoned_value} || [] ;
    if(scalar(@$lookup_result)){
    	$result->{$phoned_value} = $lookup_result ;
    }

    return $result ;
}

sub matchDoubleMetaphone {
    my ( %params ) = @_ ;

    my $result = {} ;
    my $phoned_value = join('', double_metaphone($params{value}));
    my $lookup_result = $params{lookup}->{$phoned_value} || [] ;
    if(scalar(@$lookup_result)){
    	$result->{$phoned_value} = $lookup_result ;
    }

    return $result ;
}

1;