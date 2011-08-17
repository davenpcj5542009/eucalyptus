package com.eucalyptus.records;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Entity;

@Entity
@javax.persistence.Entity
@PersistenceContext( name = "eucalyptus_records" )
@Table( name = "records_logs" )
@DiscriminatorValue( value = "base" )
public class LogFileRecord extends BaseRecord {
  private static Logger LOG = Logger.getLogger( EventRecord.class );
  @Column( name = "record_caller" )
  private String        caller;
  
  public LogFileRecord( ) {
    super( );
  }
  
  public LogFileRecord( EventClass eventClass, EventType type, Class creator, StackTraceElement callerStack, String userId, String correlationId, String other ) {
    super( type, eventClass, creator, callerStack, userId, correlationId, other );
    if ( Logs.isExtrrreeeme() ) {
      if ( callerStack != null && callerStack.getFileName( ) != null ) {
        this.caller = String.format( "   [%s.%s.%s]", callerStack.getFileName( ).replaceAll( "\\.\\w*\\b", "" ), callerStack.getMethodName( ),
                                     callerStack.getLineNumber( ) );
      } else {
        this.caller = "unknown";
      }
    }
  }
  
  public String getCaller( ) {
    return this.caller;
  }
  
  @Override
  public String toString( ) {
    if ( Logs.isExtrrreeeme() ) {
      String leadIn = String.format( "%s %s %s ",
                                        ( this.getUserId( ) != null
                                          ? this.getUserId( )
                                          : "" ),
                                         ( this.getCorrelationId( ) != null
                                             ? this.getCorrelationId( )
                                             : "" ),
                                       this.getType( ) );
      StringBuilder ret = new StringBuilder( );
      ret.append( leadIn ).append( ":" ).append( this.getCaller( ) );
      for ( Object o : this.getOthers( ) ) {
        if ( o == null ) continue;
        if ( BaseRecord.NEXT.equals( o ) ) {
          ret.append( leadIn );
        }
        ret.append( " " ).append( o.toString( ) );
      }
      return ret.toString( ).trim( );
      
    } else {
      return ( this.caller != null
        ? super.toString( ) + ":" + this.caller
        : super.toString( ) );
    }
  }
  
}
