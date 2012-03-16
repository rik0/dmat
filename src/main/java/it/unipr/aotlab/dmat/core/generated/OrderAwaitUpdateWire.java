// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: home/paolo/uni/dissertation/dmat/proto/OrderAwaitUpdateWire.proto

package it.unipr.aotlab.dmat.core.generated;

public final class OrderAwaitUpdateWire {
  private OrderAwaitUpdateWire() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface OrderAwaitUpdateBodyOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string matrixId = 1;
    boolean hasMatrixId();
    String getMatrixId();
    
    // required .RectangleBody updatingPosition = 2;
    boolean hasUpdatingPosition();
    it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getUpdatingPosition();
    it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getUpdatingPositionOrBuilder();
  }
  public static final class OrderAwaitUpdateBody extends
      com.google.protobuf.GeneratedMessage
      implements OrderAwaitUpdateBodyOrBuilder {
    // Use OrderAwaitUpdateBody.newBuilder() to construct.
    private OrderAwaitUpdateBody(Builder builder) {
      super(builder);
    }
    private OrderAwaitUpdateBody(boolean noInit) {}
    
    private static final OrderAwaitUpdateBody defaultInstance;
    public static OrderAwaitUpdateBody getDefaultInstance() {
      return defaultInstance;
    }
    
    public OrderAwaitUpdateBody getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.internal_static_OrderAwaitUpdateBody_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.internal_static_OrderAwaitUpdateBody_fieldAccessorTable;
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
    
    // required .RectangleBody updatingPosition = 2;
    public static final int UPDATINGPOSITION_FIELD_NUMBER = 2;
    private it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody updatingPosition_;
    public boolean hasUpdatingPosition() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getUpdatingPosition() {
      return updatingPosition_;
    }
    public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getUpdatingPositionOrBuilder() {
      return updatingPosition_;
    }
    
    private void initFields() {
      matrixId_ = "";
      updatingPosition_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasMatrixId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasUpdatingPosition()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getUpdatingPosition().isInitialized()) {
        memoizedIsInitialized = 0;
        return false;
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
        output.writeMessage(2, updatingPosition_);
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
          .computeMessageSize(2, updatingPosition_);
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
    
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseDelimitedFrom(
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
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody prototype) {
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
       implements it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.internal_static_OrderAwaitUpdateBody_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.internal_static_OrderAwaitUpdateBody_fieldAccessorTable;
      }
      
      // Construct using it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getUpdatingPositionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        matrixId_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (updatingPositionBuilder_ == null) {
          updatingPosition_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
        } else {
          updatingPositionBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody.getDescriptor();
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody getDefaultInstanceForType() {
        return it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody.getDefaultInstance();
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody build() {
        it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody buildPartial() {
        it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody result = new it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.matrixId_ = matrixId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (updatingPositionBuilder_ == null) {
          result.updatingPosition_ = updatingPosition_;
        } else {
          result.updatingPosition_ = updatingPositionBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody) {
          return mergeFrom((it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody other) {
        if (other == it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody.getDefaultInstance()) return this;
        if (other.hasMatrixId()) {
          setMatrixId(other.getMatrixId());
        }
        if (other.hasUpdatingPosition()) {
          mergeUpdatingPosition(other.getUpdatingPosition());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasMatrixId()) {
          
          return false;
        }
        if (!hasUpdatingPosition()) {
          
          return false;
        }
        if (!getUpdatingPosition().isInitialized()) {
          
          return false;
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
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder subBuilder = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.newBuilder();
              if (hasUpdatingPosition()) {
                subBuilder.mergeFrom(getUpdatingPosition());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setUpdatingPosition(subBuilder.buildPartial());
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
      
      // required .RectangleBody updatingPosition = 2;
      private it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody updatingPosition_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder> updatingPositionBuilder_;
      public boolean hasUpdatingPosition() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getUpdatingPosition() {
        if (updatingPositionBuilder_ == null) {
          return updatingPosition_;
        } else {
          return updatingPositionBuilder_.getMessage();
        }
      }
      public Builder setUpdatingPosition(it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody value) {
        if (updatingPositionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          updatingPosition_ = value;
          onChanged();
        } else {
          updatingPositionBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder setUpdatingPosition(
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder builderForValue) {
        if (updatingPositionBuilder_ == null) {
          updatingPosition_ = builderForValue.build();
          onChanged();
        } else {
          updatingPositionBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder mergeUpdatingPosition(it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody value) {
        if (updatingPositionBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              updatingPosition_ != it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance()) {
            updatingPosition_ =
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.newBuilder(updatingPosition_).mergeFrom(value).buildPartial();
          } else {
            updatingPosition_ = value;
          }
          onChanged();
        } else {
          updatingPositionBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder clearUpdatingPosition() {
        if (updatingPositionBuilder_ == null) {
          updatingPosition_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
          onChanged();
        } else {
          updatingPositionBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder getUpdatingPositionBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getUpdatingPositionFieldBuilder().getBuilder();
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getUpdatingPositionOrBuilder() {
        if (updatingPositionBuilder_ != null) {
          return updatingPositionBuilder_.getMessageOrBuilder();
        } else {
          return updatingPosition_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder> 
          getUpdatingPositionFieldBuilder() {
        if (updatingPositionBuilder_ == null) {
          updatingPositionBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder>(
                  updatingPosition_,
                  getParentForChildren(),
                  isClean());
          updatingPosition_ = null;
        }
        return updatingPositionBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:OrderAwaitUpdateBody)
    }
    
    static {
      defaultInstance = new OrderAwaitUpdateBody(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:OrderAwaitUpdateBody)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_OrderAwaitUpdateBody_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_OrderAwaitUpdateBody_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nAhome/paolo/uni/dissertation/dmat/proto" +
      "/OrderAwaitUpdateWire.proto\032\023RectangleWi" +
      "re.proto\"R\n\024OrderAwaitUpdateBody\022\020\n\010matr" +
      "ixId\030\001 \002(\t\022(\n\020updatingPosition\030\002 \002(\0132\016.R" +
      "ectangleBodyB%\n#it.unipr.aotlab.dmat.cor" +
      "e.generated"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_OrderAwaitUpdateBody_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_OrderAwaitUpdateBody_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_OrderAwaitUpdateBody_descriptor,
              new java.lang.String[] { "MatrixId", "UpdatingPosition", },
              it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody.class,
              it.unipr.aotlab.dmat.core.generated.OrderAwaitUpdateWire.OrderAwaitUpdateBody.Builder.class);
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
