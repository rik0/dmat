// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: home/paolo/uni/dissertation/dmat/proto/SendMatrixPiece.proto

package it.unipr.aotlab.dmat.core.generated;

public final class SendMatrixPiece {
  private SendMatrixPiece() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface BodyOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string matrixId = 1;
    boolean hasMatrixId();
    String getMatrixId();
    
    // required int32 startRow = 2;
    boolean hasStartRow();
    int getStartRow();
    
    // required int32 endRow = 3;
    boolean hasEndRow();
    int getEndRow();
    
    // required int32 startCol = 4;
    boolean hasStartCol();
    int getStartCol();
    
    // required int32 endCol = 5;
    boolean hasEndCol();
    int getEndCol();
    
    // repeated string recipient = 6;
    java.util.List<String> getRecipientList();
    int getRecipientCount();
    String getRecipient(int index);
  }
  public static final class Body extends
      com.google.protobuf.GeneratedMessage
      implements BodyOrBuilder {
    // Use Body.newBuilder() to construct.
    private Body(Builder builder) {
      super(builder);
    }
    private Body(boolean noInit) {}
    
    private static final Body defaultInstance;
    public static Body getDefaultInstance() {
      return defaultInstance;
    }
    
    public Body getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.internal_static_Body_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.internal_static_Body_fieldAccessorTable;
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
    
    // required int32 startRow = 2;
    public static final int STARTROW_FIELD_NUMBER = 2;
    private int startRow_;
    public boolean hasStartRow() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getStartRow() {
      return startRow_;
    }
    
    // required int32 endRow = 3;
    public static final int ENDROW_FIELD_NUMBER = 3;
    private int endRow_;
    public boolean hasEndRow() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getEndRow() {
      return endRow_;
    }
    
    // required int32 startCol = 4;
    public static final int STARTCOL_FIELD_NUMBER = 4;
    private int startCol_;
    public boolean hasStartCol() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public int getStartCol() {
      return startCol_;
    }
    
    // required int32 endCol = 5;
    public static final int ENDCOL_FIELD_NUMBER = 5;
    private int endCol_;
    public boolean hasEndCol() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public int getEndCol() {
      return endCol_;
    }
    
    // repeated string recipient = 6;
    public static final int RECIPIENT_FIELD_NUMBER = 6;
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
      startRow_ = 0;
      endRow_ = 0;
      startCol_ = 0;
      endCol_ = 0;
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
      if (!hasStartRow()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasEndRow()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasStartCol()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasEndCol()) {
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
        output.writeInt32(2, startRow_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, endRow_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, startCol_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeInt32(5, endCol_);
      }
      for (int i = 0; i < recipient_.size(); i++) {
        output.writeBytes(6, recipient_.getByteString(i));
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
          .computeInt32Size(2, startRow_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, endRow_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, startCol_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, endCol_);
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
    
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseDelimitedFrom(
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
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body prototype) {
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
       implements it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.BodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.internal_static_Body_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.internal_static_Body_fieldAccessorTable;
      }
      
      // Construct using it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        matrixId_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        startRow_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        endRow_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        startCol_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        endCol_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        recipient_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body.getDescriptor();
      }
      
      public it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body getDefaultInstanceForType() {
        return it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body.getDefaultInstance();
      }
      
      public it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body build() {
        it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body buildPartial() {
        it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body result = new it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.matrixId_ = matrixId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.startRow_ = startRow_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.endRow_ = endRow_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.startCol_ = startCol_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.endCol_ = endCol_;
        if (((bitField0_ & 0x00000020) == 0x00000020)) {
          recipient_ = new com.google.protobuf.UnmodifiableLazyStringList(
              recipient_);
          bitField0_ = (bitField0_ & ~0x00000020);
        }
        result.recipient_ = recipient_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body) {
          return mergeFrom((it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body other) {
        if (other == it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body.getDefaultInstance()) return this;
        if (other.hasMatrixId()) {
          setMatrixId(other.getMatrixId());
        }
        if (other.hasStartRow()) {
          setStartRow(other.getStartRow());
        }
        if (other.hasEndRow()) {
          setEndRow(other.getEndRow());
        }
        if (other.hasStartCol()) {
          setStartCol(other.getStartCol());
        }
        if (other.hasEndCol()) {
          setEndCol(other.getEndCol());
        }
        if (!other.recipient_.isEmpty()) {
          if (recipient_.isEmpty()) {
            recipient_ = other.recipient_;
            bitField0_ = (bitField0_ & ~0x00000020);
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
        if (!hasStartRow()) {
          
          return false;
        }
        if (!hasEndRow()) {
          
          return false;
        }
        if (!hasStartCol()) {
          
          return false;
        }
        if (!hasEndCol()) {
          
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
            case 16: {
              bitField0_ |= 0x00000002;
              startRow_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              endRow_ = input.readInt32();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              startCol_ = input.readInt32();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              endCol_ = input.readInt32();
              break;
            }
            case 50: {
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
      
      // required int32 startRow = 2;
      private int startRow_ ;
      public boolean hasStartRow() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getStartRow() {
        return startRow_;
      }
      public Builder setStartRow(int value) {
        bitField0_ |= 0x00000002;
        startRow_ = value;
        onChanged();
        return this;
      }
      public Builder clearStartRow() {
        bitField0_ = (bitField0_ & ~0x00000002);
        startRow_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 endRow = 3;
      private int endRow_ ;
      public boolean hasEndRow() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getEndRow() {
        return endRow_;
      }
      public Builder setEndRow(int value) {
        bitField0_ |= 0x00000004;
        endRow_ = value;
        onChanged();
        return this;
      }
      public Builder clearEndRow() {
        bitField0_ = (bitField0_ & ~0x00000004);
        endRow_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 startCol = 4;
      private int startCol_ ;
      public boolean hasStartCol() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public int getStartCol() {
        return startCol_;
      }
      public Builder setStartCol(int value) {
        bitField0_ |= 0x00000008;
        startCol_ = value;
        onChanged();
        return this;
      }
      public Builder clearStartCol() {
        bitField0_ = (bitField0_ & ~0x00000008);
        startCol_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 endCol = 5;
      private int endCol_ ;
      public boolean hasEndCol() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public int getEndCol() {
        return endCol_;
      }
      public Builder setEndCol(int value) {
        bitField0_ |= 0x00000010;
        endCol_ = value;
        onChanged();
        return this;
      }
      public Builder clearEndCol() {
        bitField0_ = (bitField0_ & ~0x00000010);
        endCol_ = 0;
        onChanged();
        return this;
      }
      
      // repeated string recipient = 6;
      private com.google.protobuf.LazyStringList recipient_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureRecipientIsMutable() {
        if (!((bitField0_ & 0x00000020) == 0x00000020)) {
          recipient_ = new com.google.protobuf.LazyStringArrayList(recipient_);
          bitField0_ |= 0x00000020;
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
        bitField0_ = (bitField0_ & ~0x00000020);
        onChanged();
        return this;
      }
      void addRecipient(com.google.protobuf.ByteString value) {
        ensureRecipientIsMutable();
        recipient_.add(value);
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:Body)
    }
    
    static {
      defaultInstance = new Body(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:Body)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Body_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Body_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n<home/paolo/uni/dissertation/dmat/proto" +
      "/SendMatrixPiece.proto\"o\n\004Body\022\020\n\010matrix" +
      "Id\030\001 \002(\t\022\020\n\010startRow\030\002 \002(\005\022\016\n\006endRow\030\003 \002" +
      "(\005\022\020\n\010startCol\030\004 \002(\005\022\016\n\006endCol\030\005 \002(\005\022\021\n\t" +
      "recipient\030\006 \003(\tB%\n#it.unipr.aotlab.dmat." +
      "core.generated"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_Body_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_Body_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Body_descriptor,
              new java.lang.String[] { "MatrixId", "StartRow", "EndRow", "StartCol", "EndCol", "Recipient", },
              it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body.class,
              it.unipr.aotlab.dmat.core.generated.SendMatrixPiece.Body.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
