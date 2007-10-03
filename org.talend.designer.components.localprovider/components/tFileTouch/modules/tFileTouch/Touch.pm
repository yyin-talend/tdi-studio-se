package tFileTouch::Touch;

require Exporter;
@ISA = qw(Exporter);
@EXPORT = qw(touch);
$VERSION = "0.02";
use warnings;
use strict;

use Carp;
use IO::File;
use File::stat;

sub new
{
    my ($caller, %arg) = @_;
    my $caller_is_obj = ref($caller);
    my $class = $caller_is_obj || $caller;
    my $self = bless{}, $class;

    my $atime_only  = $arg{atime_only} || 0; # If nonzero, change only the access time.
    my $mtime_only  = $arg{mtime_only} || 0; # If nonzero, change only the modification time.
    my $no_create   = $arg{no_create}  || 0; # If nonzero, don't create if not already there.
    my $reference   = $arg{reference};       # If defined, use this file's times instead of current time.
    my $time        = $arg{time};            # If defined, use this time instead of current time.
    my $atime       = $arg{atime};           # If defined, use this time for access time instead of current time.
    my $mtime       = $arg{mtime};           # If defined, use this time for modification time instead of current time.

    if ($atime_only && $mtime_only){
	croak("Incorrect usage: 'atime_only' and 'mtime_only' are both set - they are mutually exclusive.");
    }

    if (defined $time){
	if ((defined $atime) || (defined $mtime)){
	    croak("Incorrect usage: 'time' should not be used with either 'atime' or 'mtime' - ambiguous.");
	}
	$atime = $time unless $mtime_only;
	$mtime = $time unless $atime_only;
    }

    if (defined $reference){
	if ((defined $time) || (defined $atime) || (defined $mtime)){
	    croak("Incorrect usage: 'reference' should not be used with 'time', 'atime' or 'mtime' - ambiguous.");
	}
	if (-e $reference){
	    my $sb = stat($reference) or croak("Could not stat ($reference): $!");
	    $atime = $sb->atime unless $mtime_only;
	    $mtime = $sb->mtime unless $atime_only;
	} else {
	    croak("Reference file ($reference) does not exist");
	}
    }

    $self->{_atime}      = $atime;
    $self->{_mtime}      = $mtime;
    $self->{_no_create}  = $no_create;
    $self->{_atime_only} = $atime_only;
    $self->{_mtime_only} = $mtime_only;

    return $self;
}

sub touch
{
    my ($caller, @files) = @_;
    my $caller_is_obj = ref($caller);
    my $self;

    if ($caller_is_obj){
	$self = $caller;
    } else {
	unshift @files, $caller;
	$self->{_atime}      = undef;
	$self->{_mtime}      = undef;
	$self->{_no_create}  = 0;
	$self->{_atime_only} = 0;
	$self->{_mtime_only} = 0;
    }

    my $count = 0;

    foreach my $file (@files){
	my $time = time();
	my ($atime,$mtime);
	
	if (-e $file){
	    my $sb = stat($file) or croak("Could not stat ($file): $!");
	    $atime = $sb->atime;
	    $mtime = $sb->mtime;
	} else {
	    unless ($self->{_no_create}){
		sysopen my $fh,$file,O_WRONLY|O_CREAT or croak("Can't create $file : $!");
		close $fh or croak("Can't close $file : $!");
		$atime = $time;
		$mtime = $time;
	    }
	}
	unless ($self->{_mtime_only}){
	    $atime = $time;
	    $atime = $self->{_atime} if (defined $self->{_atime});
	}
	unless ($self->{_atime_only}){
	    $mtime = $time;
	    $mtime = $self->{_mtime} if (defined $self->{_mtime});
	}
	if (utime($atime,$mtime,$file)){
	    $count++;
	}
    }
    return $count;
}
1;


=head1 AUTHOR

Nigel Wetters Gourlay (nwetters@cpan.org)

=head1 COPYRIGHT

Copyright (c) 2001,2007 Nigel Wetters Gourlay. All Rights Reserved.
This module is free software. It may be used, redistributed
and/or modified under the same terms as Perl itself.

