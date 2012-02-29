// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: home/paolo/uni/dissertation/dmat/proto/SendMatrixPieceWire.proto

package it.unipr.aotlab.dmat.core.generated;

public final class SendMatrixPieceWire {
  private SendMatrixPieceWire() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface SendMatrixPieceBodyOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string matrixId = 1;
    boolean hasMatrixId();
    String getMatrixId();
    
    // required .RectangleBody neededPiece = 2;
    boolean hasNeededPiece();
    it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getNeededPiece();
    it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getNeededPieceOrBuilder();
    
    // required bool update = 3;
    boolean hasUpdate();
    boolean getUpdate();
    
    // repeated string recipient = 4;
    java.util.List<String> getRecipientList();
    int getRecipientCount();
    String getRecipient(int index);
  }
  public static final class SendMatrixPieceBody extends
      com.google.protobuf.GeneratedMessage
      implements SendMatrixPieceBodyOrBuilder {
    // Use SendMatrixPieceBody.newBuilder() to construct.
    private SendMatrixPieceBody(Builder builder) {
      super(builder);
    }
    private SendMatrixPieceBody(boolean noInit) {}
    
    private static final SendMatrixPieceBody defaultInstance;
    public static SendMatrixPieceBody getDefaultInstance() {
      return defaultInstance;
    }
    
    public SendMatrixPieceBody getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.internal_static_SendMatrixPieceBody_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.internal_static_SendMatrixPieceBody_fieldAccessorTable;
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
    
    // required .RectangleBody neededPiece = 2;
    public static final int NEEDEDPIECE_FIELD_NUMBER = 2;
    private it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody neededPiece_;
    public boolean hasNeededPiece() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getNeededPiece() {
      return neededPiece_;
    }
    public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getNeededPieceOrBuilder() {
      return neededPiece_;
    }
    
    // required bool update = 3;
    public static final int UPDATE_FIELD_NUMBER = 3;
    private boolean update_;
    public boolean hasUpdate() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public boolean getUpdate() {
      return update_;
    }
    
    // repeated string recipient = 4;
    public static final int RECIPIENT_FIELD_NUMBER = 4;
    private com.google.protobuf.LazyStringList recipient_;
    public java.util.List<String>
        getRecipientList() {
      return recipient_;
    }
    public int getRecipientCount() {
      return recipient_.size();
    }
    public String getRecipient(int index) {
      return recipient_.get(index);
    }
    
    private void initFields() {
      matrixId_ = "";
      neededPiece_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
      update_ = false;
      recipient_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasMatrixId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasNeededPiece()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasUpdate()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getNeededPiece().isInitialized()) {
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
        output.writeMessage(2, neededPiece_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBool(3, update_);
      }
      for (int i = 0; i < recipient_.size(); i++) {
        output.writeBytes(4, recipient_.getByteString(i));
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
          .computeMessageSize(2, neededPiece_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(3, update_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < recipient_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(recipient_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getRecipientList().size();
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
    
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseDelimitedFrom(
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
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody prototype) {
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
       implements it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.internal_static_SendMatrixPieceBody_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.internal_static_SendMatrixPieceBody_fieldAccessorTable;
      }
      
      // Construct using it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getNeededPieceFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        matrixId_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (neededPieceBuilder_ == null) {
          neededPiece_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
        } else {
          neededPieceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        update_ = false;
        bitField0_ = (bitField0_ & ~0x00000004);
        recipient_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody.getDescriptor();
      }
      
      public it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody getDefaultInstanceForType() {
        return it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody.getDefaultInstance();
      }
      
      public it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody build() {
        it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody buildPartial() {
        it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody result = new it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.matrixId_ = matrixId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (neededPieceBuilder_ == null) {
          result.neededPiece_ = neededPiece_;
        } else {
          result.neededPiece_ = neededPieceBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.update_ = update_;
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          recipient_ = new com.google.protobuf.UnmodifiableLazyStringList(
              recipient_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.recipient_ = recipient_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody) {
          return mergeFrom((it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody other) {
        if (other == it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody.getDefaultInstance()) return this;
        if (other.hasMatrixId()) {
          setMatrixId(other.getMatrixId());
        }
        if (other.hasNeededPiece()) {
          mergeNeededPiece(other.getNeededPiece());
        }
        if (other.hasUpdate()) {
          setUpdate(other.getUpdate());
        }
        if (!other.recipient_.isEmpty()) {
          if (recipient_.isEmpty()) {
            recipient_ = other.recipient_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensureRecipientIsMutable();
            recipient_.addAll(other.recipient_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasMatrixId()) {
          
          return false;
        }
        if (!hasNeededPiece()) {
          
          return false;
        }
        if (!hasUpdate()) {
          
          return false;
        }
        if (!getNeededPiece().isInitialized()) {
          
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
              if (hasNeededPiece()) {
                subBuilder.mergeFrom(getNeededPiece());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setNeededPiece(subBuilder.buildPartial());
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              update_ = input.readBool();
              break;
            }
            case 34: {
              ensureRecipientIsMutable();
              recipient_.add(input.readBytes());
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
      
      // required .RectangleBody neededPiece = 2;
      private it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody neededPiece_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder> neededPieceBuilder_;
      public boolean hasNeededPiece() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getNeededPiece() {
        if (neededPieceBuilder_ == null) {
          return neededPiece_;
        } else {
          return neededPieceBuilder_.getMessage();
        }
      }
      public Builder setNeededPiece(it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody value) {
        if (neededPieceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          neededPiece_ = value;
          onChanged();
        } else {
          neededPieceBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder setNeededPiece(
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder builderForValue) {
        if (neededPieceBuilder_ == null) {
          neededPiece_ = builderForValue.build();
          onChanged();
        } else {
          neededPieceBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder mergeNeededPiece(it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody value) {
        if (neededPieceBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              neededPiece_ != it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance()) {
            neededPiece_ =
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.newBuilder(neededPiece_).mergeFrom(value).buildPartial();
          } else {
            neededPiece_ = value;
          }
          onChanged();
        } else {
          neededPieceBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder clearNeededPiece() {
        if (neededPieceBuilder_ == null) {
          neededPiece_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
          onChanged();
        } else {
          neededPieceBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder getNeededPieceBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getNeededPieceFieldBuilder().getBuilder();
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getNeededPieceOrBuilder() {
        if (neededPieceBuilder_ != null) {
          return neededPieceBuilder_.getMessageOrBuilder();
        } else {
          return neededPiece_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder> 
          getNeededPieceFieldBuilder() {
        if (neededPieceBuilder_ == null) {
          neededPieceBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder>(
                  neededPiece_,
                  getParentForChildren(),
                  isClean());
          neededPiece_ = null;
        }
        return neededPieceBuilder_;
      }
      
      // required bool update = 3;
      private boolean update_ ;
      public boolean hasUpdate() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public boolean getUpdate() {
        return update_;
      }
      public Builder setUpdate(boolean value) {
        bitField0_ |= 0x00000004;
        update_ = value;
        onChanged();
        return this;
      }
      public Builder clearUpdate() {
        bitField0_ = (bitField0_ & ~0x00000004);
        update_ = false;
        onChanged();
        return this;
      }
      
      // repeated string recipient = 4;
      private com.google.protobuf.LazyStringList recipient_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureRecipientIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          recipient_ = new com.google.protobuf.LazyStringArrayList(recipient_);
          bitField0_ |= 0x00000008;
         }
      }
      public java.util.List<String>
          getRecipientList() {
        return java.util.Collections.unmodifiableList(recipient_);
      }
      public int getRecipientCount() {
        return recipient_.size();
      }
      public String getRecipient(int index) {
        return recipient_.get(index);
      }
      public Builder setRecipient(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureRecipientIsMutable();
        recipient_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addRecipient(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureRecipientIsMutable();
        recipient_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllRecipient(
          java.lang.Iterable<String> values) {
        ensureRecipientIsMutable();
        super.addAll(values, recipient_);
        onChanged();
        return this;
      }
      public Builder clearRecipient() {
        recipient_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
        return this;
      }
      void addRecipient(com.google.protobuf.ByteString value) {
        ensureRecipientIsMutable();
        recipient_.add(value);
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:SendMatrixPieceBody)
    }
    
    static {
      defaultInstance = new SendMatrixPieceBody(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:SendMatrixPieceBody)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_SendMatrixPieceBody_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_SendMatrixPieceBody_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n@home/paolo/uni/dissertation/dmat/proto" +
      "/SendMatrixPieceWire.proto\032\023RectangleWir" +
      "e.proto\"o\n\023SendMatrixPieceBody\022\020\n\010matrix" +
      "Id\030\001 \002(\t\022#\n\013neededPiece\030\002 \002(\0132\016.Rectangl" +
      "eBody\022\016\n\006update\030\003 \002(\010\022\021\n\trecipient\030\004 \003(\t" +
      "B%\n#it.unipr.aotlab.dmat.core.generated"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_SendMatrixPieceBody_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_SendMatrixPieceBody_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_SendMatrixPieceBody_descriptor,
              new java.lang.String[] { "MatrixId", "NeededPiece", "Update", "Recipient", },
              it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody.class,
              it.unipr.aotlab.dmat.core.generated.SendMatrixPieceWire.SendMatrixPieceBody.Builder.class);
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
