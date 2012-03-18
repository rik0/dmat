// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: home/paolo/uni/dissertation/dmat/proto/OrderSetMatrixWire.proto

package it.unipr.aotlab.dmat.core.generated;

public final class OrderSetMatrixWire {
  private OrderSetMatrixWire() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface OrderSetMatrixBodyOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string matrixId = 1;
    boolean hasMatrixId();
    String getMatrixId();
    
    // optional string chunkId = 2 [default = "default"];
    boolean hasChunkId();
    String getChunkId();
    
    // optional .RectangleBody position = 3;
    boolean hasPosition();
    it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getPosition();
    it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getPositionOrBuilder();
    
    // required string URI = 4;
    boolean hasURI();
    String getURI();
  }
  public static final class OrderSetMatrixBody extends
      com.google.protobuf.GeneratedMessage
      implements OrderSetMatrixBodyOrBuilder {
    // Use OrderSetMatrixBody.newBuilder() to construct.
    private OrderSetMatrixBody(Builder builder) {
      super(builder);
    }
    private OrderSetMatrixBody(boolean noInit) {}
    
    private static final OrderSetMatrixBody defaultInstance;
    public static OrderSetMatrixBody getDefaultInstance() {
      return defaultInstance;
    }
    
    public OrderSetMatrixBody getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.internal_static_OrderSetMatrixBody_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.internal_static_OrderSetMatrixBody_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string matrixId = 1;
    public static final int MATRIXID_FIELD_NUMBER = 1;
    private java.lang.Object matrixId_;
    public boolean hasMatrixId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getMatrixId() {
      java.lang.Object ref = matrixId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          matrixId_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getMatrixIdBytes() {
      java.lang.Object ref = matrixId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        matrixId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string chunkId = 2 [default = "default"];
    public static final int CHUNKID_FIELD_NUMBER = 2;
    private java.lang.Object chunkId_;
    public boolean hasChunkId() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getChunkId() {
      java.lang.Object ref = chunkId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          chunkId_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getChunkIdBytes() {
      java.lang.Object ref = chunkId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        chunkId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional .RectangleBody position = 3;
    public static final int POSITION_FIELD_NUMBER = 3;
    private it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody position_;
    public boolean hasPosition() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getPosition() {
      return position_;
    }
    public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getPositionOrBuilder() {
      return position_;
    }
    
    // required string URI = 4;
    public static final int URI_FIELD_NUMBER = 4;
    private java.lang.Object uRI_;
    public boolean hasURI() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getURI() {
      java.lang.Object ref = uRI_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          uRI_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getURIBytes() {
      java.lang.Object ref = uRI_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        uRI_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      matrixId_ = "";
      chunkId_ = "default";
      position_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
      uRI_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasMatrixId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasURI()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasPosition()) {
        if (!getPosition().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getMatrixIdBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getChunkIdBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, position_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getURIBytes());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getMatrixIdBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getChunkIdBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, position_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getURIBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.internal_static_OrderSetMatrixBody_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.internal_static_OrderSetMatrixBody_fieldAccessorTable;
      }
      
      // Construct using it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getPositionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        matrixId_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        chunkId_ = "default";
        bitField0_ = (bitField0_ & ~0x00000002);
        if (positionBuilder_ == null) {
          position_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
        } else {
          positionBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        uRI_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody.getDescriptor();
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody getDefaultInstanceForType() {
        return it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody.getDefaultInstance();
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody build() {
        it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody buildPartial() {
        it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody result = new it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.matrixId_ = matrixId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.chunkId_ = chunkId_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (positionBuilder_ == null) {
          result.position_ = position_;
        } else {
          result.position_ = positionBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.uRI_ = uRI_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody) {
          return mergeFrom((it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody other) {
        if (other == it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody.getDefaultInstance()) return this;
        if (other.hasMatrixId()) {
          setMatrixId(other.getMatrixId());
        }
        if (other.hasChunkId()) {
          setChunkId(other.getChunkId());
        }
        if (other.hasPosition()) {
          mergePosition(other.getPosition());
        }
        if (other.hasURI()) {
          setURI(other.getURI());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasMatrixId()) {
          
          return false;
        }
        if (!hasURI()) {
          
          return false;
        }
        if (hasPosition()) {
          if (!getPosition().isInitialized()) {
            
            return false;
          }
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              matrixId_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              chunkId_ = input.readBytes();
              break;
            }
            case 26: {
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder subBuilder = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.newBuilder();
              if (hasPosition()) {
                subBuilder.mergeFrom(getPosition());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setPosition(subBuilder.buildPartial());
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              uRI_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string matrixId = 1;
      private java.lang.Object matrixId_ = "";
      public boolean hasMatrixId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getMatrixId() {
        java.lang.Object ref = matrixId_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          matrixId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setMatrixId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        matrixId_ = value;
        onChanged();
        return this;
      }
      public Builder clearMatrixId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        matrixId_ = getDefaultInstance().getMatrixId();
        onChanged();
        return this;
      }
      void setMatrixId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        matrixId_ = value;
        onChanged();
      }
      
      // optional string chunkId = 2 [default = "default"];
      private java.lang.Object chunkId_ = "default";
      public boolean hasChunkId() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getChunkId() {
        java.lang.Object ref = chunkId_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          chunkId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setChunkId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        chunkId_ = value;
        onChanged();
        return this;
      }
      public Builder clearChunkId() {
        bitField0_ = (bitField0_ & ~0x00000002);
        chunkId_ = getDefaultInstance().getChunkId();
        onChanged();
        return this;
      }
      void setChunkId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        chunkId_ = value;
        onChanged();
      }
      
      // optional .RectangleBody position = 3;
      private it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody position_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder> positionBuilder_;
      public boolean hasPosition() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getPosition() {
        if (positionBuilder_ == null) {
          return position_;
        } else {
          return positionBuilder_.getMessage();
        }
      }
      public Builder setPosition(it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody value) {
        if (positionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          position_ = value;
          onChanged();
        } else {
          positionBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder setPosition(
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder builderForValue) {
        if (positionBuilder_ == null) {
          position_ = builderForValue.build();
          onChanged();
        } else {
          positionBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder mergePosition(it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody value) {
        if (positionBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              position_ != it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance()) {
            position_ =
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.newBuilder(position_).mergeFrom(value).buildPartial();
          } else {
            position_ = value;
          }
          onChanged();
        } else {
          positionBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder clearPosition() {
        if (positionBuilder_ == null) {
          position_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
          onChanged();
        } else {
          positionBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder getPositionBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getPositionFieldBuilder().getBuilder();
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getPositionOrBuilder() {
        if (positionBuilder_ != null) {
          return positionBuilder_.getMessageOrBuilder();
        } else {
          return position_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder> 
          getPositionFieldBuilder() {
        if (positionBuilder_ == null) {
          positionBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder>(
                  position_,
                  getParentForChildren(),
                  isClean());
          position_ = null;
        }
        return positionBuilder_;
      }
      
      // required string URI = 4;
      private java.lang.Object uRI_ = "";
      public boolean hasURI() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getURI() {
        java.lang.Object ref = uRI_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          uRI_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setURI(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        uRI_ = value;
        onChanged();
        return this;
      }
      public Builder clearURI() {
        bitField0_ = (bitField0_ & ~0x00000008);
        uRI_ = getDefaultInstance().getURI();
        onChanged();
        return this;
      }
      void setURI(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        uRI_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:OrderSetMatrixBody)
    }
    
    static {
      defaultInstance = new OrderSetMatrixBody(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:OrderSetMatrixBody)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_OrderSetMatrixBody_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_OrderSetMatrixBody_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n?home/paolo/uni/dissertation/dmat/proto" +
      "/OrderSetMatrixWire.proto\032\023RectangleWire" +
      ".proto\"o\n\022OrderSetMatrixBody\022\020\n\010matrixId" +
      "\030\001 \002(\t\022\030\n\007chunkId\030\002 \001(\t:\007default\022 \n\010posi" +
      "tion\030\003 \001(\0132\016.RectangleBody\022\013\n\003URI\030\004 \002(\tB" +
      "%\n#it.unipr.aotlab.dmat.core.generated"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_OrderSetMatrixBody_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_OrderSetMatrixBody_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_OrderSetMatrixBody_descriptor,
              new java.lang.String[] { "MatrixId", "ChunkId", "Position", "URI", },
              it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody.class,
              it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          it.unipr.aotlab.dmat.core.generated.RectangleWire.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
